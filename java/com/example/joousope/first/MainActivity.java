package com.example.joousope.first;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joousope.first.Adapter.ColorAdapter;
import com.example.joousope.first.Adapter.EmojiAdapter;
import com.example.joousope.first.Adapter.FrameAdapter;
import com.example.joousope.first.Adapter.ThumbnailAdapter;
import com.example.joousope.first.Adapter.ViewPagerAdapter;
import com.example.joousope.first.Fragment.EmojiFragment;
import com.example.joousope.first.Interface.AddFrameListener;
import com.example.joousope.first.Interface.AddTextFragmentListener;
import com.example.joousope.first.Interface.BrushFragmentListener;
import com.example.joousope.first.Interface.EmojiFragmentListener;
import com.example.joousope.first.Interface.FiltersListFragmentListener;
import com.example.joousope.first.Utils.BitmapUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;


public class MainActivity extends AppCompatActivity implements FiltersListFragmentListener,AddTextFragmentListener,AddFrameListener, EditImageFragment.EditImageFragmentListener, BrushFragment.BrushFragmentListener,EmojiFragment.EmojiFragmentListener {
    public static final String pictureName = "flash.jpg";
    public static final int PERMISSION_PICK_IMAGE = 1000;
    public static final int PERMISSION_INSERT_IMAGE = 1001;
    private static final int CAMERA_REQUEST = 1002;
    PhotoEditorView photoEditorView;
    PhotoEditor photoEditor;
    public static final int SELECT_GALLERY_IMAGE = 101;
    private static final String TAG = MainActivity.class.getSimpleName();

    CardView btn_edit, btn_brush, btn_emoji, btn_text, btn_add_text, btn_image,btn_add_frame,btn_crop;
    CardView btn_filters_list;
    //@BindView(R.id.tabs)
    //TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    Toolbar toolbar;
    Bitmap originalBitmap, filteredBitmap, finalBitmap;
    FiltersList filterList;
    CoordinatorLayout coordinatorLayout;
    EditImageFragment editImageFragment;
    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float constrantFinal = 1.0f;
    Uri image_selected_uri;
    RecyclerView.LayoutManager layoutManager;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.activity_title_main));
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        photoEditorView = (PhotoEditorView) findViewById(R.id.image_preview);
        photoEditor = new PhotoEditor.Builder(this, photoEditorView)
                .setPinchTextScalable(true)
                .setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(), "emojione-android.ttf"))
                .build();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
        btn_edit = (CardView) findViewById(R.id.btn_edit);
        //btn_filters_list = (CardView) findViewById(R.id.btn_filters_list);
        btn_brush = (CardView) findViewById(R.id.btn_brush);
        btn_add_frame=(CardView)findViewById(R.id.btn_frame);
        btn_text = (CardView) findViewById(R.id.btn_text);
        btn_image = (CardView) findViewById(R.id.btn_image);
        btn_emoji = (CardView) findViewById(R.id.btn_emoji);
        btn_crop=(CardView)findViewById(R.id.btn_crop);
        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCrop(image_selected_uri);
            }
        });
        /*btn_filters_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(filterList!=null)
               {
                   filterList.show(getSupportFragmentManager(),filterList.getTag());
               }
               else
               {
                   FiltersList filterList = FiltersList.getInstance(null);
                   filterList.setListener(MainActivity.this);
                   filterList.show(getSupportFragmentManager(), filterList.getTag());
               }
            }
        });*/
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditImageFragment editImageFragment = EditImageFragment.getInstance();
                editImageFragment.setListener(MainActivity.this);
                editImageFragment.show(getSupportFragmentManager(), editImageFragment.getTag());
            }
        });
        btn_brush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // İzin ver fırçaya
                photoEditor.setBrushDrawingMode(true);
                BrushFragment brushFragment = BrushFragment.getInstance();
                brushFragment.setListener(MainActivity.this);
                brushFragment.show(getSupportFragmentManager(), brushFragment.getTag());
            }
        });
        btn_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmojiFragment emojiFragment = EmojiFragment.getInstance();
                emojiFragment.setEmojiFragmentListener(MainActivity.this);
                emojiFragment.show(getSupportFragmentManager(), emojiFragment.getTag());
            }
        });
        btn_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTextFragment addTextFragment = AddTextFragment.getInstance();
                addTextFragment.setAddTextFragmentListener(MainActivity.this);
                addTextFragment.show(getSupportFragmentManager(), addTextFragment.getTag());
            }
        });
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImageToPicture();
            }
        });
        btn_add_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameFragment frameFragment=FrameFragment.getInstance();
                frameFragment.setListener(MainActivity.this);
                frameFragment.show(getSupportFragmentManager(),frameFragment.getTag());
            }
        });
        //thumbnailItems = new ThumbnailItem();

        /*listener = new FiltersListFragmentListener() {
            @Override
            public void onFilterSelected(Filter filter) {
                return;
            }
        };*/
        loadImage();
        setupViewPager(viewPager);
    }

    private void startCrop(Uri uri) {
        String destinationFileName=new StringBuilder(UUID.randomUUID().toString()).append("jpg").toString();
        UCrop ucrop=UCrop.of(uri,uri.fromFile(new File(getCacheDir(),destinationFileName)));
        ucrop.start(MainActivity.this);
    }

    private void addImageToPicture() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_INSERT_IMAGE  );
                        } else {
                            Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
                    }
                }).check();
    }
    private void loadImage() {
        originalBitmap= BitmapUtils.getBitmapFromAssets(this,pictureName,300,300);
        filteredBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        finalBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(originalBitmap);

        /*image_preview.setImageResource(R.drawable.ic_launcher_background);
      /*  originalBitmap=((BitmapDrawable)image_preview.getDrawable()).getBitmap();

        Log.d("Debug","Merhaba8");
        /*img_preview.setImageDrawable(originalBitmap);*/
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        filterList=new FiltersList();
        filterList.setListener(this);

        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);
        viewPagerAdapter.addFragment(filterList,"FILTERS");
        viewPagerAdapter.addFragment(editImageFragment,"EDIT");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBrushSizeChangedListener(float size) {
        photoEditor.setBrushSize(size);
    }

    @Override
    public void onBrushOpacityChangedListener(int size) {
        photoEditor.setOpacity(size);
    }

    @Override
    public void onBrushColorChangedListener(int color) {
        photoEditor.setBrushColor(color);
    }

    @Override
    public void onBrushStateChangedListener(boolean isEraser) {
        if(isEraser)
            photoEditor.brushEraser();
        else
            photoEditor.setBrushDrawingMode(true);
    }



    @Override
    public void onBrightnessChanged(int brightness) {
        brightnessFinal=brightness;
        Filter myFilter=new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));

        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }



    @Override
    public void onSaturationChanged(final float saturation) {
        saturationFinal=saturation;
        Filter myFilter=new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onConstrantChanged(final float constrant) {
        constrantFinal=constrant;
        Filter myFilter=new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(constrant));

        photoEditorView.getSource().setImageBitmap(myFilter.processFilter(finalBitmap.copy(Bitmap.Config.ARGB_8888,true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        final Bitmap bitmap=filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);
        Filter myFilter=new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));

        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));

        myFilter.addSubFilter(new ContrastSubFilter(constrantFinal));
        finalBitmap=myFilter.processFilter(bitmap);
    }


    @Override
    public void onFilterSelected(Filter filter) {

        //resetControl();
        filteredBitmap=originalBitmap.copy(Bitmap.Config.ARGB_8888,true);
        photoEditorView.getSource().setImageBitmap(filter.processFilter(filteredBitmap));
        finalBitmap=filteredBitmap.copy(Bitmap.Config.ARGB_8888,true);
    }


    private void resetControl() {
        if(editImageFragment!=null)
        {
            editImageFragment.resetControls();
        }
        brightnessFinal=0;
        saturationFinal=1.0f;
        constrantFinal=1.0f;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_open)
        {
            openImageFromGallery();
            return true;
        }
        else if(id==R.id.action_save) {

            saveImageToGallery();
            return true;
        }
        else if(id==R.id.action_camera)
        {
            openCamera();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCamera() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                                ContentValues values=new ContentValues();
                                values.put(MediaStore.Images.Media.TITLE,"New Picture");
                                values.put(MediaStore.Images.Media.DESCRIPTION,"From Camera");
                                image_selected_uri=getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
                                Intent cameraIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_selected_uri);
                                startActivityForResult(cameraIntent,CAMERA_REQUEST);

                        } else {
                            Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }

    private void saveImageToGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                @Override
                                public void onBitmapReady(Bitmap saveBitmap) {
                                    photoEditorView.getSource().setImageBitmap(saveBitmap);

                                    final String path = BitmapUtils.insertImage(getContentResolver(), saveBitmap,
                                            System.currentTimeMillis() + "_profile.jpg", null);
                                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
                                    if (!TextUtils.isEmpty(path)) {
                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Image saved to gallery", Snackbar.LENGTH_LONG)
                                                .setAction("OPEN", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        openImage(path);
                                                    }
                                                });
                                        snackbar.show();

                                    } else {
                                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Unable to save image", Snackbar.LENGTH_LONG);
                                        snackbar.show();
                                    }
                                }
                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        } else {
                            Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openImageFromGallery() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            startActivityForResult(intent, PERMISSION_PICK_IMAGE);
                        } else {
                            Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode==RESULT_OK)
        {
            if(requestCode==PERMISSION_PICK_IMAGE) {
                Bitmap bitmap = BitmapUtils.getBipmapFromGallery(this, data.getData(), 800, 800);
                image_selected_uri=data.getData();

                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();
                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);

                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();

                filterList=FiltersList.getInstance(originalBitmap);
                filterList.setListener(this);
            }
            if(requestCode==CAMERA_REQUEST) {
                Bitmap bitmap = BitmapUtils.getBipmapFromGallery(this, image_selected_uri, 800, 800);


                originalBitmap.recycle();
                finalBitmap.recycle();
                filteredBitmap.recycle();
                originalBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                finalBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true);

                photoEditorView.getSource().setImageBitmap(originalBitmap);
                bitmap.recycle();

                filterList=FiltersList.getInstance(originalBitmap);
                filterList.setListener(this);
            }
            else if(requestCode==PERMISSION_INSERT_IMAGE)
            {
                Bitmap bitmap=BitmapUtils.getBipmapFromGallery(this,data.getData(),250,250);
                photoEditor.addImage(bitmap);
            }
            else if(requestCode==UCrop.REQUEST_CROP)
                handleCropResult(data);

        }
        else if(resultCode==UCrop.RESULT_ERROR)
            handleCropError(data);
    }

    private void handleCropError(Intent data) {
        final Throwable cropError=UCrop.getError(data);
        if(cropError!=null)
        {
            Toast.makeText(this,""+cropError.getMessage(),Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"Unexpected Error",Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(Intent data) {
        final Uri resultUri=UCrop.getOutput(data);
        if(resultUri!=null)
        {
            photoEditorView.getSource().setImageURI(resultUri);
            Bitmap bitmap=((BitmapDrawable)photoEditorView.getSource().getDrawable()).getBitmap();
            originalBitmap=bitmap.copy(Bitmap.Config.ARGB_8888,true);
            filteredBitmap=originalBitmap;
        }
        else
        {
            Toast.makeText(this,"Cannot retrieve crop image",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    private void openImage(String path) {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(path),"image/*");
        startActivity(intent);
    }

    @Override
    public void onEmojiSelected(String emoji) {
            photoEditor.addEmoji(emoji);
    }

    @Override
    public void onAddTextButtonClick(String text, int color) {
        photoEditor.addText(text,color);
    }

    @Override
    public void onAddFrame(int frame) {
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),frame);
        photoEditor.addImage(bitmap);
    }
}
