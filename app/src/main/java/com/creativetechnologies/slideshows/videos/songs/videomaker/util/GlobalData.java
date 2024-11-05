package com.creativetechnologies.slideshows.videos.songs.videomaker.util;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.R;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.AdModel;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.CategoryModel;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.SubCatModel;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.SharedPrefs;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Harshad Kathiriya on 11/12/2016.
 */

public class GlobalData {
    public static boolean is_home_back = true;

    public static ArrayList<AdModel> full_ad = new ArrayList<>();

    public static boolean is_start = false;
    public static Boolean APD_FLAG = false;
    //public static int AD_index;

    //App_Center
    public static ArrayList<CategoryModel> al_app_center_data = new ArrayList<>();
    public static int screenWidth = 0;
    public static int screenHeight = 0;
    public static ArrayList<AdModel> al_ad_data = new ArrayList<>();
    public static String selected_tab = "HOME";
    public static int position = 0;
    public static ArrayList<CategoryModel> al_app_center_home_data = new ArrayList<>();

    //SplashContent
    public static ArrayList<File> al_ad_full_image_from_storage = new ArrayList<>();
    public static ArrayList<AdModel> al_ad_full_image_from_api = new ArrayList<>();
    public static int AD_index;
    public static ArrayList<String> al_ad_full_image_name = new ArrayList<>();
    public static ArrayList<SubCatModel> more_apps_data = new ArrayList<>();
    public static String ntv_img;
    public static String ntv_inglink;
    public static boolean is_button_click = false;
    public static ArrayList<String> al_ad_package_name = new ArrayList<>();

    public class KEYNAME {
        public static final String ALBUM_NAME = "album_name";
    }

    //Load Banner for Google
    public static void loadAdsBanner(Activity activity, AdView adView) {
        try {

            boolean isNeedToShow = false;

            if (!SharedPrefs.contain(activity, SharedPrefs.IS_ADS_REMOVED)) {
                isNeedToShow = true;
            } else {
                if (!SharedPrefs.getBoolean(activity, SharedPrefs.IS_ADS_REMOVED))
                    isNeedToShow = true;
                else
                    isNeedToShow = false;
            }

            if (isNeedToShow) {
                adView = (AdView) activity.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .addTestDevice("E19949FB5E7C5A28C30A875934AC8181") //SWIPE
                        .addTestDevice("41E9C9F5D1F985FB36C9760EFC8F3916") //Lenovo
                        .addTestDevice("64A3A22A05D9DCDBEC68395FF5048CD1")  //Coolpad
                        .addTestDevice("51A49E7B1B359D1999E5C85CE4F54978") //XOLO
                        .addTestDevice("F9EBC1840023CB004A83005514278635") //MI 6otu (No SIM)
                        .addTestDevice("FB7A4409DCE756E8A6102AD2F92E4C05") //Micromax New
                        .addTestDevice("DAA085BEA3A93989A854942ADACFDB2E")  //INTEX
                        .addTestDevice("A7A19E06342F7D3868ABA7863D707BD7") //Samsung Tab
                        .addTestDevice("78E289C0CB209B06541CB844A1744650") //LAVA
                        .addTestDevice("29CB61C62E053C3C348D8549D6DAAD47")//X-ZIOX
                        .addTestDevice("A3B40000C5F87634ABA0FF2655637306") //Celkon
                        .addTestDevice("BEAA671BEA6C971FE461AC6E423B2ADE") //Karbonn
                        .addTestDevice("74527FD0DD7B0489CFB68BAED192733D") //Nexus TAB
                        .addTestDevice("BB5542D48765B65F516CF440C3545896") //Samsung j2
                        .addTestDevice("E56855A0C493CEF11A7098FE6EA840CB") //Videocon
                        .addTestDevice("390FED1AE343E9FF9D644C4085C3868E") //jivi
                        .addTestDevice("ACFC7B7082B3F3FD4E0AC8E92EA10D53") //MI Tab
                        .addTestDevice("863D8BAE88E209F38FF3C94A0403C776") //Samsung old
                        .addTestDevice("CF03A7085F307594629D95E17F811FB2") //Samsung new
                        .addTestDevice("517048997101BE4535828AC2360582C2") //motorola
                        .addTestDevice("8BB4BCB27396AB8ED222B7F902E13420") //micromax old
                        .addTestDevice("0876F1300137A59971BA9CA9C6876AB8") //Gionee
                        .addTestDevice("F7803FE72A2748F6028D87DC36D7C574") //Mi Chhotu JIO
                        .addTestDevice("CEF2CF599FA65D8072F04888C122999E") //iVoomi
                        .addTestDevice("BB5542D48765B65F516CF440C3545896") //samusung j2
                        .addTestDevice("DD0A309E21D1F24C324C107BE78C1B88") //Ronak Oreo
                        .addTestDevice("B05DBFFC98F6E3E7A8E75E2FE96C2D65") //Nokia Oreo
                        .addTestDevice("E19949FB5E7C5A28C30A875934AC8181") //HiTech
                        .addTestDevice("87F69E04FBB38A9B1A18D828ACDA4E38")
                        .addTestDevice("152B6C19B7D96AACCA727D2A6A3EDE36")
                        .addTestDevice("553B57A7B0422031839D1F2CC0607EB8")
                        .addTestDevice("BEAA671BEA6C971FE461AC6E423B2ADE")
                        .addTestDevice("708CD90C669969A8EF3B86337DF15B03")
                        .addTestDevice("BB455EC66CACD5BED7C9DA3058817519")
                        .addTestDevice("2A6E3914633DA48BB7E9B7E5BE42E0A3")
                        .addTestDevice("5AA511ABBA217E08FDFF986C5217F238")
                        .addTestDevice("35BFFAB134DE8961215FD8F37C935429")
                        .addTestDevice("E3836C31FFC091D23960B2B7BC2C65A7")
                        .addTestDevice("5DB1A5C691809BA8986E958BDD47122F")
                        .addTestDevice("74EB0D80BBB7A2CD6A71EAF4726DB4B6")
                        .addTestDevice("C9A4EC8B86692E53985E9E827DB9E890")
                        .addTestDevice("44D80701BA3ED7E56B47396B857DD653")
                        .addTestDevice("567DB1C5EC4A5D581176C2652228829D")
                        .addTestDevice("E3B60CE263CD4E5886434E9F34FB21E5")
                        .build();
                adView.loadAd(adRequest);

                final AdView finalAdView = adView;

                adView.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        finalAdView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        finalAdView.setVisibility(View.VISIBLE);
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}