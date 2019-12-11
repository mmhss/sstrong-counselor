package com.hsd.avh.standstrong.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.tabs.TabLayout;
import com.hornet.dateconverter.DateConverter;
import com.hornet.dateconverter.DatePicker.DatePickerDialog;
import com.hornet.dateconverter.Model;
import com.hsd.avh.standstrong.R;
import com.hsd.avh.standstrong.StandStrong;
import com.hsd.avh.standstrong.managers.AnalyticsManager;
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil;
import com.hsd.avh.standstrong.viewmodels.PersonDetailViewModel;
import com.hsd.avh.standstrong.viewmodels.PersonDetailViewModelFactory;
import com.hsd.avh.standstrong.viewmodels.PostListViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.collection.ArrayMap;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.hsd.avh.standstrong.utilities.InjectorUtils;
import com.hsd.avh.standstrong.viewmodels.PostListViewModelFactory;

import javax.inject.Inject;


public class FilterPostFabFragment extends AAH_FabulousFragment implements DatePickerDialog.OnDateSetListener   {

    private PostListViewModel viewModel;
    ArrayMap<String, List<String>> applied_filters = new ArrayMap<>();
    List<TextView> textviews = new ArrayList<>();
    TabLayout tabs_types;
    SectionsPagerAdapter mAdapter;
    TextView outputDatePicker;
    TextView outputConversion;
    DateConverter dateConverter = new DateConverter();

    @Inject
    AnalyticsManager analyticsManager;


    public static FilterPostFabFragment newInstance() {
        FilterPostFabFragment f = new FilterPostFabFragment();
        return f;
    }




    @Override
    public void onResume() {
        super.onResume();
        FirebaseTrackingUtil fb = new FirebaseTrackingUtil(getActivity());
        fb.track(FirebaseTrackingUtil.Screens.Filter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applied_filters = StandStrong.Companion.getFilters();
        final PostListViewModelFactory factory= InjectorUtils.INSTANCE.providePostListViewModelFactory(getActivity());
        viewModel = ViewModelProviders.of(getActivity(),factory).get(PostListViewModel.class);


        ((StandStrong) getActivity().getApplication()).managerComponent.inject(this);
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.filter_main_view, null);
        RelativeLayout rl_content = (RelativeLayout) contentView.findViewById(R.id.rl_content);
        LinearLayout ll_buttons = (LinearLayout) contentView.findViewById(R.id.ll_buttons);

        ViewPager vp_types = (ViewPager) contentView.findViewById(R.id.vp_types);
        tabs_types = (TabLayout) contentView.findViewById(R.id.tabs_types);


        contentView.findViewById(R.id.imgbtn_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setFilter(applied_filters);
                closeFilter(applied_filters);

                Bundle args = new Bundle();

                StringBuilder compositeFilter = new StringBuilder();

                for (List<String> lists : applied_filters.values()) {

                    for (String s : lists) {
                        compositeFilter.append(s);
                        compositeFilter.append(",");
                    }
                }

                args.putString("Filters post", compositeFilter.toString());

                analyticsManager.trackEvent("Selected filters for person", args);
            }
        });

        contentView.findViewById(R.id.imgbtn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (TextView tv : textviews) {
                    tv.setTag("unselected");
                    tv.setBackgroundResource(R.drawable.filter_chip_unselected);
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filter_chip));
                }
                applied_filters.clear();
                viewModel.clearFilter();
                closeFilter(applied_filters);

                analyticsManager.trackEvent("Clear filer click", null);
            }
        });

        mAdapter = new SectionsPagerAdapter();
        vp_types.setOffscreenPageLimit(4);
        vp_types.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        tabs_types.setupWithViewPager(vp_types);


        //params to set
        setAnimationDuration(600); //optional; default 500ms
        setPeekHeight(300); // optional; default 400dp
        //setCallbacks((Callbacks) getActivity()); //optional; to get back result
        //setAnimationListener((AnimationListener) getActivity()); //optional; to get animation callbacks
        setViewgroupStatic(ll_buttons); // optional; layout to stick at bottom on slide
        //setViewPager(vp_types); //optional; if you use viewpager that has scrollview
        setViewMain(rl_content); //necessary; main bottomsheet view
        setMainContentView(contentView); // necessary; call at end before super
        super.setupDialog(dialog, style); //call super at last
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: " + dayOfMonth + " " + getResources().getString(DateConverter.getNepaliMonthString(monthOfYear)) + " " + year;
        outputDatePicker.setText(date);

        //Month of year starts with 0, thus adding +1 will change month to 1-12
        monthOfYear = monthOfYear + 1;

        Model engDate = dateConverter.getEnglishDate(year, monthOfYear, dayOfMonth);
        String d = "" + engDate.getYear() + " " + getEnglishMonth(engDate.getMonth()) + " " +
                engDate.getDay();
        outputConversion.setText(d);

        int month = engDate.getMonth()+1;

        addToSelectedMap("date", ""+engDate.getYear() +"-"+month+"-"+  engDate.getDay());
    }

    public String getEnglishMonth(int month) {
        switch (month) {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "";
    }

    public class SectionsPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            ViewGroup layout = null;

//            LinearLayout ll_scroll = (LinearLayout) layout.findViewById(R.id.ll_scroll);
//            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (metrics.heightPixels-(104*metrics.density)));
//            ll_scroll.setLayoutParams(lp);
            switch (position) {
                case 0:
                    layout = (ViewGroup) inflater.inflate(R.layout.filter_sorters, collection, false);
                    FlexboxLayout fbl = (FlexboxLayout) layout.findViewById(R.id.fbl);
                    inflateLayoutWithFilters("type", fbl);
                    break;
                case 1:
                    layout = (ViewGroup) inflater.inflate(R.layout.filter_date, collection, false);
                    inflateLayoutTwo(layout);
                    break;
            }
            collection.addView(layout);
            return layout;

        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "FILTER";
                case 1:
                    return "DATE";
            }
            return "";
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


    }

    private void inflateLayoutTwo(ViewGroup layout) {
        final Button datePicker  = ((Button) layout.findViewById(R.id.materialDatePickerButton));
        outputConversion  = ((TextView) layout.findViewById(R.id.outputConvertTextView));
        outputDatePicker  = ((TextView) layout.findViewById(R.id.outputDatePickerTextView));


        DatePickerDialog dpd = DatePickerDialog.newInstance(this,dateConverter.getNepaliDate(Calendar.getInstance()));
        dpd.setVersion(DatePickerDialog.Version.VERSION_2);
        dpd.setThemeDark(false);
        dpd.dismissOnPause(true);
        dpd.showYearPickerFirst(false);
        dpd.setAccentColor(Color.parseColor("#9C27B0"));
        dpd.setTitle("DatePicker Title");
        //dpd.setMinDate(dateConverter.getTodayNepaliDate());
        //dpd.setHighlightedDays(getSampleModelList());
        // dpd.setSelectableDays(getSampleModelList());
        //dpd.setDisabledDays(getSampleModelList());

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();

             //   if (v.getId() == R.id.materialDatePickerButton) {
            /*
            DatePickerDialog dpd = DatePickerDialog.newInstance(MainActivity.this,
                    2015,
                    3,
                    17);*/
            /*
            DatePickerDialog dpd = DatePickerDialog.newInstance(MainActivity.this,
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));
            */


                dpd.show( getFragmentManager() , "DatePicker");
                    return;


                /*    int yy = Integer.parseInt(year.getText().toString());
                    int mm = Integer.parseInt(month.getText().toString());
                    int dd = Integer.parseInt(day.getText().toString());

                    switch (v.getId()) {
                        case R.id.adToBsConvertButton:
                            try {
                                Model nepDate = dateConverter.getNepaliDate(yy, mm, dd);
                                String date = "" + nepDate.getYear() + " " + getResources().getString(DateConverter.getNepaliMonthString(nepDate.getMonth())) + " " +
                                        nepDate.getDay() + " " + getDayOfWeek(nepDate.getDayOfWeek());
                                outputConversion.setText(date);
                            } catch (IllegalArgumentException e) {
                                Toast.makeText(MainActivity.this, "Date out of Range", Toast.LENGTH_LONG).show();
                            }
                            break;
                        case R.id.bsToAdConvertButton:
                            try {
                                Model engDate = dateConverter.getEnglishDate(yy, mm, dd);
                                String date = "" + engDate.getYear() + " " + getEnglishMonth(engDate.getMonth()) + " " +
                                        engDate.getDay() + " " + getDayOfWeek(engDate.getDayOfWeek());
                                outputConversion.setText(date);
                            } catch (IllegalArgumentException e) {
                                Toast.makeText(MainActivity.this, "Date out of Range", Toast.LENGTH_LONG).show();
                            }
                            break;

                    }*/

            }
        });
    }



    private void inflateLayoutWithFilters(final String filter_category, FlexboxLayout fbl) {
        List<String> keys = new ArrayList<>();
        keys.add(StandStrong.POST_CARD_STRING_PROXIMITY);
        keys.add(StandStrong.POST_CARD_STRING_AWARD);
        keys.add(StandStrong.POST_CARD_STRING_GPS);
        keys.add(StandStrong.POST_CARD_STRING_ACTIVITY);
        keys.add(StandStrong.POST_CARD_STRING_MESSAGE);
        keys.add(StandStrong.POST_CARD_STRING_GOAL);


        for (int i = 0; i < keys.size(); i++) {
            View subchild = getActivity().getLayoutInflater().inflate(R.layout.filter_single_chip, null);
            final TextView tv = ((TextView) subchild.findViewById(R.id.txt_title));
            tv.setText(keys.get(i));
            final int finalI = i;
            final List<String> finalKeys = keys;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv.getTag() != null && tv.getTag().equals("selected")) {
                        tv.setTag("unselected");
                        tv.setBackgroundResource(R.drawable.filter_chip_unselected);
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filter_chip));
                        removeFromSelectedMap(filter_category, finalKeys.get(finalI));
                    } else {
                        tv.setTag("selected");
                        tv.setBackgroundResource(R.drawable.filter_chip_selected);
                        tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                        addToSelectedMap(filter_category, finalKeys.get(finalI));
                    }

                    Bundle args = new Bundle();
                    args.putString("Type", filter_category);
                    analyticsManager.trackEvent("On filter chip click", args);
                }
            });
            try {
                Log.d("k9res", "key: " + filter_category + " |val:" + keys.get(finalI));
                Log.d("k9res", "applied_filters != null: " + (applied_filters != null));
                Log.d("k9res", "applied_filters.get(key) != null: " + (applied_filters.get(filter_category) != null));
                Log.d("k9res", "applied_filters.get(key).contains(keys.get(finalI)): " + (applied_filters.get(filter_category).contains(keys.get(finalI))));
            } catch (Exception e) {

            }
            if (applied_filters != null && applied_filters.get(filter_category) != null && applied_filters.get(filter_category).contains(keys.get(finalI))) {
                tv.setTag("selected");
                tv.setBackgroundResource(R.drawable.filter_chip_selected);
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
            } else {
                tv.setBackgroundResource(R.drawable.filter_chip_unselected);
                tv.setTextColor(ContextCompat.getColor(getContext(), R.color.filter_chip));
            }
            textviews.add(tv);

            fbl.addView(subchild);
        }


    }

    private void addToSelectedMap(String key, String value) {
        if (applied_filters.get(key) != null && !applied_filters.get(key).contains(value)) {
            applied_filters.get(key).add(value);

        } else {
            List<String> temp = new ArrayList<>();
            temp.add(value);
            applied_filters.put(key, temp);
        }
    }

    private void removeFromSelectedMap(String key, String value) {
        if (applied_filters.get(key).size() == 1) {
            applied_filters.remove(key);
        } else {
            applied_filters.get(key).remove(value);
        }
    }

}
