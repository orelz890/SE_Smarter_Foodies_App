package com.example.smarter_foodies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnLogOut;
    Button btnAddRecipe;
//    TabLayout tabLayout;
//    ViewPager viewPager;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        new scraper("https://www.10dakot.co.il/");

        btnLogOut = findViewById(R.id.btnLogout);
        btnAddRecipe = findViewById(R.id.btnAddRecipe);
//        tabLayout=findViewById(R.id.tab_layout);
//        viewPager=findViewById(R.id.view_pager);

//        // Initialize array list
//        ArrayList<String> arrayList=new ArrayList<>(0);
//
//        // Add title in array list
//        arrayList.add("Basic");
//        arrayList.add("Advance");
//        arrayList.add("Pro");

        // Setup tab layout
//        tabLayout.setupWithViewPager(viewPager);

        // Prepare view pager
//        prepareViewPager(viewPager,arrayList);

        btnAddRecipe.setOnClickListener(view ->{
            startActivity(new Intent(MainActivity.this, AddRecipe.class));
        });

        mAuth = FirebaseAuth.getInstance();
        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginGoogle.class));
        });

    }
//    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {
//        // Initialize main adapter
//        MainAdapter adapter=new MainAdapter(getSupportFragmentManager());
//
//        // Initialize main fragment
//        MainFragment mainFragment=new MainFragment();
//
//        // Use for loop
//        for(int i=0;i<arrayList.size();i++)
//        {
//            // Initialize bundle
//            Bundle bundle=new Bundle();
//
//            // Put title
//            bundle.putString("title",arrayList.get(i));
//
//            // set argument
//            mainFragment.setArguments(bundle);
//
//    // Add fragment
//            adapter.addFragment(mainFragment,arrayList.get(i));
//    mainFragment=new MainFragment();
//}
//// set adapter
//        viewPager.setAdapter(adapter);
//                }
//
//    private class MainAdapter extends FragmentPagerAdapter {
//        // Initialize arrayList
//        ArrayList<Fragment> fragmentArrayList= new ArrayList<>();
//        ArrayList<String> stringArrayList=new ArrayList<>();
//
//        int[] imageList={R.drawable.basic,R.drawable.advance,R.drawable.pro};
//
//        // Create constructor
//        public void addFragment(Fragment fragment,String s)
//        {
//            // Add fragment
//            fragmentArrayList.add(fragment);
//            // Add title
//            stringArrayList.add(s);
//        }
//
//        public MainAdapter(FragmentManager supportFragmentManager) {
//            super(supportFragmentManager);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position) {
//            // return fragment position
//            return fragmentArrayList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            // Return fragment array list size
//            return fragmentArrayList.size();
//        }
//
//        @Nullable
//        @Override
//        public CharSequence getPageTitle(int position) {
//
//            // Initialize drawable
//            Drawable drawable= ContextCompat.getDrawable(getApplicationContext()
//                    ,imageList[position]);
//
//            // set bound
//            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),
//                    drawable.getIntrinsicHeight());
//
//            // Initialize spannable image
//            SpannableString spannableString=new SpannableString(""+stringArrayList.get(position));
//
//            // Initialize image span
//            ImageSpan imageSpan=new ImageSpan(drawable,ImageSpan.ALIGN_BOTTOM);
//
//            // Set span
//            spannableString.setSpan(imageSpan,0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            // return spannable string
//            return spannableString;
//        }
//    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginGoogle.class));
        }
    }
}