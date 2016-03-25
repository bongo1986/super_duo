package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Greg on 24-03-2016.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    ArrayList<MatchInfo> mCollections = new ArrayList<MatchInfo>();

    Context mContext = null;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }


    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
//        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        MatchInfo currentMatch = mCollections.get(position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(),R.layout.score_list_widget_item);

        views.setTextViewText(R.id.home_name, currentMatch.home_name);
        views.setTextViewText(R.id.away_name, currentMatch.away_name);
        views.setTextViewText(R.id.data_textview, currentMatch.matchtime);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {
        mCollections.clear();

        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new java.util.Date());
        String now_date = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        String where = DatabaseContract.scores_table.DATE_TIME_COL + " = " + "'" + now + "'";

        Cursor cursor = mContext.getContentResolver().query(
                DatabaseContract.BASE_CONTENT_URI, new String[]{ DatabaseContract.scores_table.AWAY_COL,
                        DatabaseContract.scores_table.AWAY_GOALS_COL,
                        DatabaseContract.scores_table.DATE_COL,
                        DatabaseContract.scores_table.HOME_COL,
                        DatabaseContract.scores_table.AWAY_COL,
                        DatabaseContract.scores_table.HOME_GOALS_COL,
                        DatabaseContract.scores_table.LEAGUE_COL,
                        DatabaseContract.scores_table.MATCH_ID,
                        DatabaseContract.scores_table.TIME_COL },
                        DatabaseContract.scores_table.DATE_COL + " = '" + now_date + "' and " + DatabaseContract.scores_table.DATE_TIME_COL + " > " + "Datetime('" + now + "')",
                null,
                DatabaseContract.scores_table.DATE_TIME_COL + " ASC " + " LIMIT 3");

        try {
        while(cursor.moveToNext()){
            MatchInfo mi = new MatchInfo();
            mi.home_name = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
            mi.away_name = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
            mi.date = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.DATE_COL));
            mi.matchtime = cursor.getString(cursor.getColumnIndex(DatabaseContract.scores_table.TIME_COL));
            mi.homeGoals = cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
            mi.awayGoals = cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));
            mi.match_id = cursor.getDouble(cursor.getColumnIndex(DatabaseContract.scores_table.MATCH_ID));
            mCollections.add(mi);
        }
        }
        finally {
            cursor.close();
        }

    }

    private class MatchInfo{
        public String home_name;
        public String away_name;
        public String date;
        public String matchtime;
        public int homeGoals;
        public int awayGoals;
        public double match_id;
    }
}


