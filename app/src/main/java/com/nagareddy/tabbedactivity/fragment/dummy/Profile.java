package com.nagareddy.tabbedactivity.fragment.dummy;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.nagareddy.tabbedactivity.R;

import java.util.List;

/**
 * Created by keerthichandra on 2/28/2016.
 */
public class Profile extends ArrayAdapter<String>{


    ListView list;
    String[] itemname = {"Memphis", "UoM", "FedEx"  };

    Integer[] imgid = {R.drawable.mute, R.drawable.ringer, R.drawable.vibrate};


    public Profile(Context context, int resource, List<String> objects) {


        super(context, resource, objects);
    }
}
