package com.creativetechnologies.slideshows.videos.songs.videomaker;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.creativetechnologies.slideshows.videos.songs.videomaker.adapter.FontAdapter;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.FontModel;
import com.creativetechnologies.slideshows.videos.songs.videomaker.model.TEXT_MODEL;
import com.creativetechnologies.slideshows.videos.songs.videomaker.share.Share;
import com.creativetechnologies.slideshows.videos.songs.videomaker.util.GlobalData;

import java.util.ArrayList;

public class FontActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar_top;
    private EditText et_text;
    private ImageView iv_color, iv_done;
    private RecyclerView rv_font;
    private LinearLayout ll_font_color;
    private FontAdapter fontAdapter;
    private ArrayList<FontModel> list = new ArrayList<>();
    private String font_array[] = {"1", "6", "ardina_script", "beyondwonderland", "C", "coventry_garden_nf", "font3", "font6", "font10", "font16", "font20", "g", "h", "h2", "h3", "h6", "h7", "h8", "h15", "h18", "h19", "h20", "m", "o", "saman", "variane", "youmurdererbb"};
    private AdView adView;
    public static ImageView iv_more_app, iv_blast;
    public static Boolean is_closed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font);

        if (Share.RestartAppForOnlyStorage(FontActivity.this)) {
           // mFirebaseAnalytics = FirebaseAnalytics.getInstance(FontActivity.this);

            GlobalData.loadAdsBanner(FontActivity.this, adView);
            initView();
            loadFontStyle();
        }
    }

    private void initView() {

        iv_more_app = (ImageView) findViewById(R.id.iv_more_app);
        iv_blast = (ImageView) findViewById(R.id.iv_blast);

        if (Share.isNeedToAdShow(getApplicationContext())) {
            iv_more_app.setVisibility(View.GONE);
            iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
            ((AnimationDrawable) iv_more_app.getBackground()).start();
            loadInterstialAd();
        }

        et_text = (EditText) findViewById(R.id.et_text);
        toolbar_top = (Toolbar) findViewById(R.id.toolbar_top);
        ll_font_color = (LinearLayout) findViewById(R.id.ll_font_color);
        iv_color = (ImageView) findViewById(R.id.iv_color);
        iv_done = (ImageView) findViewById(R.id.iv_done);

        rv_font = (RecyclerView) findViewById(R.id.rv_font);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FontActivity.this, 3);
        rv_font.setLayoutManager(gridLayoutManager);

        rv_font.getLayoutParams().height = GlobalData.screenHeight - toolbar_top.getHeight() - ll_font_color.getHeight();

        et_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });

        iv_color.setOnClickListener(this);
        iv_done.setOnClickListener(this);
        iv_more_app.setOnClickListener(this);

        et_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });

        if (Share.FLAG) {
            et_text.setText("");
        } else {

            if (CustomSlideActivity.start_end_tab_flag) {
                if (Share.START_FONT_TEXT2.size() != 0) {
                    et_text.setText(Share.START_FONT_TEXT2.get(Share.START_STICKER_POSITION).getName());
                    et_text.setTextColor(Share.START_FONT_TEXT2.get(Share.START_STICKER_POSITION).getColors());
                    et_text.setTypeface(Share.START_FONT_TEXT2.get(Share.START_STICKER_POSITION).getTypeface());
                    int position = Share.START_FONT_TEXT2.get(Share.START_STICKER_POSITION).getName().length();
                    et_text.setSelection(position);
                }
            } else {
                if (Share.FONT_TEXT2.size() != 0) {
                    et_text.setText(Share.FONT_TEXT2.get(Share.STICKER_POSITION).getName());
                    et_text.setTextColor(Share.FONT_TEXT2.get(Share.STICKER_POSITION).getColors());
                    et_text.setTypeface(Share.FONT_TEXT2.get(Share.STICKER_POSITION).getTypeface());
                    int position = Share.FONT_TEXT2.get(Share.STICKER_POSITION).getName().length();
                    et_text.setSelection(position);
                }
            }
        }
    }

    private void loadFontStyle() {

        list.clear();

        for (int i = 0; i < font_array.length; i++) {
            FontModel spinnerModel = new FontModel();
            spinnerModel.setFont_name(font_array[i]);
            list.add(spinnerModel);
        }

        fontAdapter = new FontAdapter(FontActivity.this, list);
        rv_font.setAdapter(fontAdapter);

        fontAdapter.setEventListener(new FontAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {

                Share.FONT_EFFECT = font_array[position].toString().toLowerCase();

                Typeface face = Typeface.createFromAsset(FontActivity.this.getAssets(), font_array[position].toLowerCase() + ".ttf");
                et_text.setTypeface(face);
            }

            @Override
            public void onDeleteMember(int position) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == iv_color) {

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_text.getWindowToken(), 0);

            ColorPickerDialogBuilder
                    .with(FontActivity.this)
                    .setTitle("Choose color")
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {
                        }
                    })
                    .setPositiveButton("ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            et_text.setTextColor(selectedColor);
                            Share.COLOR = selectedColor;
                            Log.e("TAG", "selected Color :" + Share.COLOR);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .build()
                    .show();
        } else if (v == iv_done) {
//            Share.EDIT_FLAG = true;

            if (et_text.getText().toString().equals("")) {
                Toast.makeText(FontActivity.this, "Please Enter Text", Toast.LENGTH_SHORT).show();
            } else {

                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_text.getWindowToken(), 0);

                if (Share.FLAG) {
                    Share.FLAG = false;

                    if (Share.isNeedToAdShow(getApplicationContext())) {
                        if (!MyApplication.getInstance().requestNewInterstitial()) {

                            redirectActivity();
                        } else {

                            MyApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                    MyApplication.getInstance().mInterstitialAd.setAdListener(null);
                                    MyApplication.getInstance().mInterstitialAd = null;
                                    MyApplication.getInstance().ins_adRequest = null;
                                    MyApplication.getInstance().LoadAds();

                                    redirectActivity();
                                }

                                @Override
                                public void onAdFailedToLoad(int i) {
                                    super.onAdFailedToLoad(i);
                                }

                                @Override
                                public void onAdLoaded() {
                                    super.onAdLoaded();
                                }
                            });
                        }
                    } else {
                        redirectActivity();
                    }

                } else {

                    if (CustomSlideActivity.start_end_tab_flag) {
                        if (Share.START_TEXT_EDIT_FLAG) {

                            TEXT_MODEL model = new TEXT_MODEL(et_text.getText().toString().trim(), et_text.getTypeface(), et_text.getCurrentTextColor());
                            Share.START_FONT_TEXT2.set(Share.START_STICKER_POSITION, model);

                            Bitmap b2 = createBitmapFromLayoutWithText(getApplicationContext(), et_text.getText().toString().trim(), et_text.getCurrentTextColor(), 0, et_text.getTypeface());
                            Drawable d = new BitmapDrawable(getResources(), b2);
                            Share.EDIT_DAILOG_DRAWABLE = d;


                        } else {
                            Share.TEXT = et_text.getText().toString();

                            TEXT_MODEL model = new TEXT_MODEL(et_text.getText().toString().trim(), et_text.getTypeface(), et_text.getCurrentTextColor());
                            Share.START_FONT_TEXT2.add(model);


                            Bitmap b2 = createBitmapFromLayoutWithText(getApplicationContext(), et_text.getText().toString().trim(), et_text.getCurrentTextColor(), 0, et_text.getTypeface());
                            Drawable d = new BitmapDrawable(getResources(), b2);
                            Share.EDIT_DAILOG_DRAWABLE = d;

                        }
                    } else {
                        if (Share.TEXT_EDIT_FLAG) {

                            TEXT_MODEL model = new TEXT_MODEL(et_text.getText().toString().trim(), et_text.getTypeface(), et_text.getCurrentTextColor());
                            Share.FONT_TEXT2.set(Share.STICKER_POSITION, model);

                            Bitmap b2 = createBitmapFromLayoutWithText(getApplicationContext(), et_text.getText().toString().trim(), et_text.getCurrentTextColor(), 0, et_text.getTypeface());
                            Drawable d = new BitmapDrawable(getResources(), b2);
                            Share.EDIT_DAILOG_DRAWABLE = d;
                        } else {
                            Share.TEXT = et_text.getText().toString();

                            TEXT_MODEL model = new TEXT_MODEL(et_text.getText().toString().trim(), et_text.getTypeface(), et_text.getCurrentTextColor());
                            Share.FONT_TEXT2.add(model);


                            Bitmap b2 = createBitmapFromLayoutWithText(getApplicationContext(), et_text.getText().toString().trim(), et_text.getCurrentTextColor(), 0, et_text.getTypeface());
                            Drawable d = new BitmapDrawable(getResources(), b2);
                            Share.EDIT_DAILOG_DRAWABLE = d;

                        }
                    }

                    if (Share.isNeedToAdShow(getApplicationContext())) {
                        if (!MyApplication.getInstance().requestNewInterstitial()) {

                            redirectActivityFirstTime();
                        } else {

                            MyApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                                @Override
                                public void onAdClosed() {
                                    super.onAdClosed();
                                    MyApplication.getInstance().mInterstitialAd.setAdListener(null);
                                    MyApplication.getInstance().mInterstitialAd = null;
                                    MyApplication.getInstance().ins_adRequest = null;
                                    MyApplication.getInstance().LoadAds();

                                    redirectActivityFirstTime();
                                }

                                @Override
                                public void onAdFailedToLoad(int i) {
                                    super.onAdFailedToLoad(i);
                                }

                                @Override
                                public void onAdLoaded() {
                                    super.onAdLoaded();
                                }
                            });
                        }
                    } else {
                        redirectActivityFirstTime();
                    }

                }
            }
        } else if (v == iv_more_app) {
            is_closed = false;
            iv_more_app.setVisibility(View.GONE);
            iv_blast.setVisibility(View.VISIBLE);
            ((AnimationDrawable) iv_blast.getBackground()).start();

            if (MyApplication.getInstance().requestNewInterstitial()) {
                MyApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.e("ad cloced", "ad closed");
                        iv_blast.setVisibility(View.GONE);
                        iv_more_app.setVisibility(View.GONE);
                        is_closed = true;
                        /*iv_more_app.setBackgroundResource(R.drawable.animation_list_filling);
                        ((AnimationDrawable) iv_more_app.getBackground()).start();*/
                        loadInterstialAd();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        iv_blast.setVisibility(View.GONE);
                        iv_more_app.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        is_closed = false;
                        iv_blast.setVisibility(View.GONE);
                        iv_more_app.setVisibility(View.GONE);
                    }
                });
            } else {
                iv_blast.setVisibility(View.GONE);
                iv_more_app.setVisibility(View.GONE);
            }
        }
    }

    private void redirectActivityFirstTime() {

        finish();
    }

    private void redirectActivity() {

        if (CustomSlideActivity.start_end_tab_flag) {
            TEXT_MODEL model = new TEXT_MODEL(et_text.getText().toString().trim(), et_text.getTypeface(), et_text.getCurrentTextColor());
            Share.START_FONT_TEXT2.add(model);

            Bitmap b2 = createBitmapFromLayoutWithText(getApplicationContext(), et_text.getText().toString().trim(), et_text.getCurrentTextColor(), 0, et_text.getTypeface());
            Drawable d = new BitmapDrawable(getResources(), b2);
            Share.EDIT_DAILOG_DRAWABLE = d;
            finish();
        } else {
            TEXT_MODEL model = new TEXT_MODEL(et_text.getText().toString().trim(), et_text.getTypeface(), et_text.getCurrentTextColor());
            Share.FONT_TEXT2.add(model);

            Bitmap b2 = createBitmapFromLayoutWithText(getApplicationContext(), et_text.getText().toString().trim(), et_text.getCurrentTextColor(), 0, et_text.getTypeface());
            Drawable d = new BitmapDrawable(getResources(), b2);
            Share.EDIT_DAILOG_DRAWABLE = d;
            finish();
        }
    }

    public static Bitmap createBitmapFromLayoutWithText(Context context, String s, int color, int i, Typeface face) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.row_bitmap, null);

        TextView tv = (TextView) view.findViewById(R.id.tv_custom_text1);

        for (int j = 0; j < s.length(); j += 50) {
            if (s.length() >= 50) {
                if (j <= s.length() - 50) {
                    if (j == 0) {
                        String m = s.substring(0, 50);
                        tv.setText(m);
                    } else {
                        tv.append("\n");
                        String l = s.substring(j, j + 50);
                        tv.append(l);
                    }
                } else {
                    tv.append("\n");
                    String l = s.substring(j, s.length());
                    tv.append(l);
                }
            } else {
                tv.setText(s);
            }
        }
        //  tv.setText(s);
        tv.setTextColor(color);
        tv.setTypeface(face);
        view.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        view.draw(c);
        return bitmap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (Share.FLAG) {
            Share.TEXT = "";
            Share.FLAG = false;
            Share.FONT_TEXT_DRAWABLE = null;
            Share.START_TEXT_EDIT_FLAG = false;
            Share.TEXT_EDIT_FLAG = false;
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        } else {
            Share.START_TEXT_EDIT_FLAG = false;
            Share.TEXT_EDIT_FLAG = false;
            finish();
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
        }
    }

    private void loadInterstialAd() {
        try {
            if (MyApplication.getInstance().mInterstitialAd != null) {
                if (MyApplication.getInstance().mInterstitialAd.isLoaded()) {
                    Log.e("if", "if");
                    iv_more_app.setVisibility(View.VISIBLE);
                } else {
                    MyApplication.getInstance().mInterstitialAd.setAdListener(null);
                    MyApplication.getInstance().mInterstitialAd = null;
                    MyApplication.getInstance().ins_adRequest = null;
                    MyApplication.getInstance().LoadAds();
                    MyApplication.getInstance().mInterstitialAd.setAdListener(new AdListener() {
                        @Override
                        public void onAdLoaded() {
                            super.onAdLoaded();
                            iv_more_app.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            iv_more_app.setVisibility(View.GONE);
                            //ColoringApplication.getInstance().LoadAds();
                            loadInterstialAd();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!Share.resume_flag) {
            if (Share.isNeedToAdShow(getApplicationContext()))
                if (is_closed) {
                    loadInterstialAd();
                }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Share.resume_flag = false;
    }
}
