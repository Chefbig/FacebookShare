package ca.ncai.facebookshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.facebook.login.DefaultAudience;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class PermissionSelectActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = PermissionSelectActivity.class.getSimpleName();

    public static final String EXTRA_SELECTED_READ_PARAMS = TAG + ".selectedReadPerms";
    public static final String EXTRA_SELECTED_WRITE_PRIVACY = TAG + ".selectedWritePrivacy";
    public static final String EXTRA_SELECTED_PUBLISH_PARAMS = TAG + ".selectedPublishPerms";

    // Permissions
    public static String PUBLISH_ACTIONS = "publish_actions";
    public static String PUBLISH_CHECKINS = "publish_checkins";
    public static String ADS_MANAGEMENT = "ads_management";
    public static String CREATE_EVENT = "create_event";
    public static String MANAGE_FRIENDLISTS = "manage_friendlists";
    public static String MANAGE_NOTIFICATIONS = "manage_notifications";
    public static String MANAGE_PAGES = "manage_pages";
    public static String RSVP_EVENT = "rsvp_event";

    public static final Set<String> PUBLISH_PERMS_LIST = new HashSet<>(Arrays.asList(
            PUBLISH_ACTIONS,
            PUBLISH_CHECKINS,
            ADS_MANAGEMENT,
            CREATE_EVENT,
            MANAGE_FRIENDLISTS,
            MANAGE_NOTIFICATIONS,
            MANAGE_PAGES,
            RSVP_EVENT));

    Button button;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_select);
        listView = (ListView) findViewById(R.id.list);
        button = (Button) findViewById(R.id.confirm);
        String[] perms = getResources().getStringArray(R.array.perms_array);

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, perms);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        button.setOnClickListener(this);
    }

    public void onClick(View v) {
        SparseBooleanArray checked = listView.getCheckedItemPositions();
        ArrayList<String> readPerms = new ArrayList<>();
        String writePri = null;
        ArrayList<String> publishPerms = new ArrayList<>();
        for (int i = 0; i < checked.size(); i++) {
            // Item position in adapter
            int position = checked.keyAt(i);
            // Add perm only if checked
            if (checked.valueAt(i)) {
                String checkedItem = adapter.getItem(position);
                if (DefaultAudience.EVERYONE.toString().equals(checkedItem)) {
                    writePri = "EVERYONE";
                } else if (DefaultAudience.FRIENDS.toString().equals(checkedItem)) {
                    writePri = "FRIENDS";
                } else if (DefaultAudience.ONLY_ME.toString().equals(checkedItem)) {
                    writePri = "ONLY_ME";
                } else if ((PUBLISH_PERMS_LIST).contains(checkedItem)) {
                    publishPerms.add(checkedItem);
                } else
                    readPerms.add(checkedItem);
            }
        }

        String[] readPermsArr = readPerms.toArray(new String[readPerms.size()]);
        String[] publishPermsArr = publishPerms.toArray(new String[publishPerms.size()]);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_READ_PARAMS, readPermsArr);
        intent.putExtra(EXTRA_SELECTED_WRITE_PRIVACY, writePri);
        intent.putExtra(EXTRA_SELECTED_PUBLISH_PARAMS, publishPermsArr);
        setResult(RESULT_OK, intent);
        finish();
    }
}
