package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;


import java.text.SimpleDateFormat;

import barqsoft.footballscores.service.WidgetService;

/**
 * Created by Greg on 21-03-2016.
 */
public class ScoresAppWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        boolean anyMatchToday = false;
        String  home_name = "";
        String away_name = "";
        String date = "";
        String matchtime = "";
        int homeGoals = 0;
        int awayGoals = 0;
        Double match_id;

        String now = new SimpleDateFormat("yyyy.MM.dd HH:mm").format(new java.util.Date());

        Cursor cursor = context.getContentResolver().query(
                DatabaseContract.BASE_CONTENT_URI, new String[]{
                        DatabaseContract.scores_table.AWAY_COL
                },
                "'" + DatabaseContract.scores_table.DATE_TIME_COL + "'" + " > " + "'" + now + "'",
                null,
                DatabaseContract.scores_table.DATE_TIME_COL + " ASC " + " LIMIT 3");

        if(cursor.getCount() > 0){
            anyMatchToday = true;
        }

        for(int i=0; i<appWidgetIds.length; i++){


            int currentWidgetId = appWidgetIds[i];

            RemoteViews mView = new RemoteViews(context.getPackageName(),R.layout.scores_list_widget);

            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            mView.setRemoteAdapter(appWidgetIds[i], R.id.widgetCollectionList, intent);

            if(anyMatchToday) {
                mView.setViewVisibility(R.id.widgetCollectionList, View.VISIBLE);
                mView.setViewVisibility(R.id.noRestultsContainer, View.GONE);

            }
            else{
                mView.setViewVisibility(R.id.noRestultsContainer, View.VISIBLE);
                mView.setViewVisibility(R.id.widgetCollectionList, View.GONE);
            }
            appWidgetManager.updateAppWidget(currentWidgetId, mView);

        }
    }
}
