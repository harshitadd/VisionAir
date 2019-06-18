package com.example.features;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.opencv.imgproc.Imgproc.INTER_AREA;
import static org.opencv.imgproc.Imgproc.matchShapes;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private TextView textView,textContrst;
    public static final int REQUEST_IMAGE_CAPTURE = 100;
    private ImageView imageView;
    private Mat img,dest;
    private Button buttonTakePicture,buttonFeatures;
    private Uri file;
    Bitmap imageBitmap;
    public static final int REQUEST_TAKE_PHOTO = 100;
    Uri photoURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text_view);
        imageView = (ImageView) findViewById(R.id.image_view);
        textContrst = (TextView) findViewById(R.id.text_Contrast);
        buttonTakePicture = (Button) findViewById(R.id.buttonTake);
        buttonFeatures = (Button) findViewById(R.id.buttonShow);

        buttonTakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        buttonFeatures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readImageFromResources();

                transmission();
        contrast();
        entropy();


            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            buttonTakePicture.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        OpenCVLoader.initDebug();
//        readImageFromResources();

    }
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Toast.makeText(getApplicationContext(),"dhf "+currentPhotoPath,Toast.LENGTH_LONG).show();
        return image;
    }


    public void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
            imageBitmap =null;
            try {
                imageBitmap= MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(imageBitmap);
        }
    }



    private Mat readImageFromResources() {
        img = new Mat();

            Utils.bitmapToMat(imageBitmap,img);
            Toast.makeText(getApplicationContext(),"Image is loaded",Toast.LENGTH_SHORT).show();

        return img;
    }
    private void showImage(Mat img){
        Bitmap bm = Bitmap.createBitmap(img.cols(), img.rows(),Bitmap.Config.RGB_565);
        Utils.matToBitmap(img, bm);

        imageView.setImageBitmap(bm);
    }
    private void transmission(){

        //to resize image
        dest = new Mat();
        Size scaleSize = new Size(256,256);
        Imgproc.resize(img,dest, scaleSize , 0, 0, INTER_AREA);
        Toast.makeText(getApplicationContext(),"get size"+dest.size(),Toast.LENGTH_LONG).show();


        //convert bgr to rgb
        Imgproc.cvtColor(dest, dest, Imgproc.COLOR_BGR2RGB);
        Toast.makeText(getApplicationContext(),"get size"+img.size(),Toast.LENGTH_LONG).show();


        //store values of r,g,b
        List<Mat> lRgb = new ArrayList<Mat>(3);
        Core.split(img, lRgb);
        Mat mR = lRgb.get(0);
        Mat mG = lRgb.get(1);
        Mat mB = lRgb.get(2);
        Toast.makeText(getApplicationContext(),"RED:"+mR+"Green:"+mG+"Blue:"+mB,Toast.LENGTH_LONG).show();
//        textView.setText("Red"+mR.dump());
//        Log.i(TAG,"Red"+mR.dump());

        //convert rgb to hsv
        Mat hsvImage = new Mat();
        Imgproc.cvtColor(dest,hsvImage,Imgproc.COLOR_RGB2HSV);
        Toast.makeText(getApplicationContext(),"Image is in HSV",Toast.LENGTH_SHORT).show();

        //get hsv values
        List<Mat> Rgb = new ArrayList<Mat>(3);
        Core.split(hsvImage, Rgb);
        Mat mH = Rgb.get(0);
        Mat mS = Rgb.get(1);
        Mat mV = Rgb.get(2);
        Toast.makeText(getApplicationContext(),"H: "+mH+"S:"+mS+"V:"+mV,Toast.LENGTH_LONG).show();
        Log.i(TAG,"H: "+mH+"S:"+mS+"V:"+mV);
//        textView.setText("Hsv image"+hsvImage.dump());


        //to make image smooth
        Mat v_mask = new Mat();
        int kernel_size=3;
        Mat ker = new Mat();
        Mat kernel = Mat.ones(kernel_size,kernel_size, CvType.CV_32F);
        Core.multiply(kernel,new Scalar(1/(double)(kernel_size*kernel_size)),ker);

        Imgproc.filter2D(mV, v_mask, -1, ker);


        //threshold
        Mat build = new Mat();
        Imgproc.threshold(v_mask, build, 230, 255, Imgproc.THRESH_BINARY);


        //sky
        Mat sky = new Mat();
        Core.bitwise_and(dest,dest,sky,build);


        //skyMask
        Mat skyMask = new Mat();
        Core.bitwise_not(build,skyMask);

        //building
        Mat building = new Mat();
        Core.bitwise_and(dest,dest,building,skyMask);


        //to find air light
        Core.MinMaxLocResult minMaxLocResult = Core.minMaxLoc(v_mask);
        float al = (float) minMaxLocResult.maxVal;



        //to find transition map
        Mat C = dest.clone();
        C.convertTo(C, CvType.CV_64FC3);
        double[] a = new double[(int) (dest.total()*dest.channels())];
        C.get(0,0,a);
        for(int i = 0; i < a.length; i ++){
            a[i] = a[i]/al;
        }
        C.put(0,0,a);

//
//
        //to find r,g,b values
        List<Mat> Rgb3 = new ArrayList<Mat>(3);

        Core.split(C, Rgb3);
        Mat iR = Rgb3.get(0);
        Mat iG = Rgb3.get(1);
        Mat iB = Rgb3.get(2);
        Mat dc = iR.clone();
        int rows = iR.rows();
        int cols = iR.cols();

        for (int i = 0; i < dest.rows(); i++) {
            for (int j = 0; j < dest.cols(); j++) {
                double min = Math.min(iR.get(i, j)[0], Math.min(iG.get(i, j)[0], iB.get(i, j)[0]));
                dc.put(i, j, min);
            }
        }

        Mat b1 = new Mat();

        double krnlRatio = 1;
        int krnlSz = Double.valueOf(Math.max(Math.max(rows * krnlRatio, cols * krnlRatio), 3.0)).intValue();
        Mat kere = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(krnlSz, krnlSz), new Point(-1, -1));
        Log.i(TAG,"susi"+kere.size()+" "+kere.channels()+" "+kere.type());
        Log.i(TAG,"dc"+dc.size()+" "+dc.channels());
        Imgproc.erode(dc, b1, kere);

//
//
//
//
        Mat tr = dc.clone();
        Mat dr = new Mat();
        dr = Mat.ones(dc.rows(),dc.cols(),CvType.CV_8UC1);
        Mat trList = dr.clone();
        tr.convertTo(tr, CvType.CV_64FC3);
        trList.convertTo(trList,CvType.CV_64FC3);
        double[] b = new double[(int) (tr.total()*tr.channels())];
        double [] l = new double[(int)(trList.total()*trList.channels())];
        tr.get(0,0,b);
        trList.get(0,0,l);
        for(int i = 0; i < b.length; i++){
            l[i] = 1-b[i];
        }
        trList.put(0,0,l);

//
//        Mat he = new Mat();
//        he = trList.clone();
//        double [] k = new double[(int)(he.total()*he.channels())];
//        he.convertTo(he,CvType.CV_64FC3);
//        he.get(0,0,k);
//        for(int i=0;i<k.length;i++){
//            k[i]=((k[i]-(Core.minMaxLoc(he).minVal)))/(float)(255*(Core.minMaxLoc(he).maxVal- Core.minMaxLoc(he).minVal));
//        }
//        he.put(0,0,k);
//        for (int i = 0; i < he.rows(); i++) {
//            for (int j = 0; j < he.cols(); j++) {
//                values1.add((float)he.get(i,j)[0]);
//            }
//        }
//        textView.setText("dhihdfi"+he);
//
//
//
//        Mat t1 = new Mat();
//        textView.setText("values: "+values1);
//
//            he.convertTo(t1,CvType.CV_8UC4);

       // textView.setText("dhidf"+(Core.minMaxLoc(trList).maxVal - Core.minMaxLoc(trList).minVal));

        //textView.setText("C:"+C.dump());
        Mat img_norm = new Mat();
        Core.normalize(trList,img_norm,0,255,Core.NORM_MINMAX,CvType.CV_8UC1);

         showImage(img_norm);
//       values(img_norm);
    }


    public void contrast(){

        //to resize image
        dest = new Mat();
        Size scaleSize = new Size(256,256);
        Imgproc.resize(img,dest, scaleSize , 0, 0, INTER_AREA);
        Toast.makeText(getApplicationContext(),"get size"+dest.size(),Toast.LENGTH_LONG).show();


        //convert bgr to gray
        Imgproc.cvtColor(dest, dest, Imgproc.COLOR_BGR2GRAY);
        Toast.makeText(getApplicationContext(),"get size"+img.size(),Toast.LENGTH_LONG).show();

        float s =0;
        float ss =0;

        Mat con = dest.clone();
        con.convertTo(con, CvType.CV_64FC3);
        double[] a = new double[(int) (dest.total()*dest.channels())];
        con.get(0,0,a);
        for(int i = 0; i < a.length; i ++){
            s = (int) (s+a[i]);
        }
        con.put(0,0,s);

        float avg ;
        avg = (s/(256*256));

        Mat co = dest.clone();
        co.convertTo(co, CvType.CV_64FC3);
        double[] b = new double[(int) (dest.total()*dest.channels())];
        co.get(0,0,b);
        for(int i = 0; i < b.length; i++){
            ss = (float) (ss + (float)((b[i]-avg)*(b[i]-avg)));
        }
        co.put(0,0,ss);

        float contra;
        contra = (float) Math.sqrt(ss/(256*256));
        textContrst.setText("Contrast: "+contra);
    }

    public void entropy(){
        //to resize image
        dest = new Mat();
        Size scaleSize = new Size(256,256);
        Imgproc.resize(img,dest, scaleSize , 0, 0, INTER_AREA);
        Toast.makeText(getApplicationContext(),"get size"+dest.size(),Toast.LENGTH_LONG).show();


        //convert bgr to gray
        Imgproc.cvtColor(dest, dest, Imgproc.COLOR_BGR2GRAY);
        Toast.makeText(getApplicationContext(),"get size"+img.size(),Toast.LENGTH_LONG).show();

        //to calculate histogram
        MatOfInt histSize = new MatOfInt(256);

        Mat hist = new Mat(dest.size(),dest.type());



        ArrayList<Mat> list = new ArrayList<Mat>();
        list.add(dest);


        Imgproc.calcHist(list, new MatOfInt(0), new Mat(), hist, histSize, new MatOfFloat(0, 256));
        Core.normalize(hist,hist);

        //to find entropy
//        double ent = 0;
//        Mat h = hist.clone();
//        double[] b = new double[(int)(hist.channels()*hist.total())];
//        for(int i=5;i<256;i++) {
//            try {
//                ent += b[i] * Math.log(b[i]);
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
//            }
//            textView.setText("degfu= "+ent);
//        }

        double total = 0;
        double ent =0;
        for (int row = 0; row < hist.rows(); row++) {
            double[] val = hist.get(row, 0);
                for(int p=0;p<val.length;p++) {
                    try {
                        if(val[p]!= 0.0){
                        ent += val[p]*(Math.log(val[p])/Math.log(2));}
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
            }

                textView.setText("Entropy: "+ent);
        }

//        values(hist);
    }

    public void values(Mat img){
        List<Float> value = new ArrayList<>();

        for(int i=0;i<img.rows();i++){
            for(int j=0;j<img.cols();j++){
                value.add((float)img.get(i,j)[0]);
            }
        }
        textView.setText("Values: "+value);
    }

}
