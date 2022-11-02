package com.ajinkya.prettymeal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajinkya.prettymeal.adapter.MessCardAdapter;
import com.ajinkya.prettymeal.model.MessInfo;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private ImageSlider imageSlider;
    private RecyclerView recyclerView;
    private ArrayList<MessInfo> messInfoArrayList;
    private SearchView searchView;
    private MessCardAdapter messCardAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Initialize(view);

        setCards();
        Buttons();

        return view;
    }

    private void Buttons() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<MessInfo> filterList = new ArrayList<>();
                for(MessInfo messInfo: messInfoArrayList){
                    if(messInfo.getMessName().toLowerCase().contains(newText.toLowerCase())){
                        filterList.add(messInfo);
                    }
                }

                if(filterList.isEmpty()){
                    Toast.makeText(getContext(), "Sorry Mess Not Available", Toast.LENGTH_SHORT).show();
                }else {
                    messCardAdapter.setFilteredList(filterList,getContext());
                }
                return true;
            }
        });
    }

    private void Initialize(View view) {
        imageSlider = view.findViewById(R.id.ImageSlider);
        recyclerView = view.findViewById(R.id.messCardRecyclerView);
        searchView = view.findViewById(R.id.SearchView);
        searchView.clearFocus();



        List<SlideModel> ImagesList = new ArrayList<>();
//        ImagesList.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        ImagesList.add(new SlideModel("https://d3jmn01ri1fzgl.cloudfront.net/photoadking/webp_thumbnail/5fe3257ad6874_json_image_1608721786.webp", ScaleTypes.FIT));
        ImagesList.add(new SlideModel("https://i.pinimg.com/736x/da/66/24/da66249a283dafab8488b5c3bddf56f1.jpg", ScaleTypes.FIT));
        ImagesList.add(new SlideModel("https://i.ytimg.com/vi/mnCDSmooRxA/maxresdefault.jpg", ScaleTypes.FIT));
        imageSlider.setImageList(ImagesList, ScaleTypes.FIT);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setCards() {
        messInfoArrayList = new ArrayList<>();
        MessInfo messInfo = new MessInfo();
        messInfo.setMessName("Hotel Meal Magic");
        messInfo.setMessLocation("Front of Raisoni College, Wagholi, Pune-412207");
        messInfo.setBannerImgURL("https://img.restaurantguru.com/r696-Hotel-Saravana-Bhavan-food-2021-09-38.jpg");
        messInfo.setPrice("$60");
        messInfo.setVeg_nonVeg("PureVeg");
        messInfo.setTodayVegMenu("Chole bhaji , chapati , Upama , Dal , Rice , Salad");
        messInfo.setTodayNonVegMenu("NA");
        messInfoArrayList.add(messInfo);


        MessInfo messInfo1 = new MessInfo();
        messInfo1.setMessName("Gharchi Athawan");
        messInfo1.setMessLocation("Wagholi, Pune-412207");
        messInfo1.setBannerImgURL("https://png.pngtree.com/thumb_back/fh260/back_our/20190620/ourmid/pngtree-food-seasoning-food-banner-image_169169.jpg");
        messInfo1.setPrice("$60");
        messInfo1.setVeg_nonVeg("Veg-NonVeg");
        messInfo1.setTodayVegMenu("Chole bhaji , chapati , Upama , Dal , Rice , Salad");
        messInfo1.setTodayNonVegMenu("Chicken kari , Chapati , Salad");
        messInfoArrayList.add(messInfo1);

        MessInfo messInfo2 = new MessInfo();
        messInfo2.setMessName("Swami Samartha");
        messInfo2.setMessLocation("Wagholi, Pune-412207");
        messInfo2.setBannerImgURL("https://png.pngtree.com/thumb_back/fh260/back_our/20190620/ourmid/pngtree-food-seasoning-food-banner-image_169169.jpg");
        messInfo2.setPrice("$60");
        messInfo2.setVeg_nonVeg("PureVeg");
        messInfo2.setTodayVegMenu("Chole bhaji , chapati , Upama , Dal , Rice , Salad");
        messInfo2.setTodayNonVegMenu("NA");
        messInfoArrayList.add(messInfo2);


        MessInfo messInfo3 = new MessInfo();
        messInfo3.setMessName("Jay Malhar");
        messInfo3.setMessLocation("Wagholi, Pune-412207");
        messInfo3.setBannerImgURL("https://png.pngtree.com/thumb_back/fh260/back_our/20190620/ourmid/pngtree-food-seasoning-food-banner-image_169169.jpg");
        messInfo3.setPrice("$60");
        messInfo3.setVeg_nonVeg("Veg-NonVeg");
        messInfo3.setTodayVegMenu("Chole bhaji , chapati , Upama , Dal , Rice , Salad");
        messInfo3.setTodayNonVegMenu("Chicken kari , Chapati , Salad");
        messInfoArrayList.add(messInfo3);


        messCardAdapter = new MessCardAdapter(messInfoArrayList, getContext());
        recyclerView.setAdapter(messCardAdapter);
    }


}