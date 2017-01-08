package com.example.adm.color;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "OCVSample::Activity";
    private Button mButton = null;

    //public Bitmap bitmap = null;
    private final static int RESULT_CAMERA = 1001;
    private Uri mImageUri;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mButton.setEnabled(true);
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "called onCreate");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mButton = (Button) findViewById(R.id.button1);

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String filename = System.currentTimeMillis() + ".jpg";

                //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //startActivityForResult(intent, RESULT_CAMERA);

                //File file = new File("/storage/sdcard1/sotuken/DSC_0028_1.jpg");
                //Bitmap bmp1 = BitmapFactory.decodeFile(file.getPath());

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                mImageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                startActivityForResult(intent, RESULT_CAMERA);


            }

        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_CAMERA) {
            //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Bitmap bitmap = BitmapFactory.decodeFile(mImageUri.getPath());

//rensyuu
            /*
            Mat aaa = new Mat();
            Mat bbb = new Mat();

            Utils.bitmapToMat(bitmap, aaa);



            Imgproc.cvtColor(aaa, bbb, Imgproc.COLOR_RGB2HSV, 0);

            Bitmap bmp2 = Bitmap.createBitmap(bbb.cols(), bbb.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(bbb, bmp2);

            mButton.setVisibility(View.INVISIBLE);
           ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bmp2);
*/


            TextView rv = (TextView) findViewById(R.id.rView);


            Mat mat1 = new Mat();
            Mat mathsv = new Mat();



            //Utils.bitmapToMat(bmp1, mat1);


            Utils.bitmapToMat(bitmap, mat1);



            Imgproc.cvtColor(mat1, mathsv, Imgproc.COLOR_RGB2HSV, 0);


            // Hue is 180 not 360

            //Imgproc.morphologyEx(matc, molfe,Imgproc.MORPH_OPEN, new Mat(), new Point(-1,-1),3);
            //Imgproc.morphologyEx(molfe, molfd,Imgproc.MORPH_OPEN, new Mat(), new Point(-1,-1),5);
            //Imgproc.dilate(matc, molfe,Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 10)), new Point(-1, -1), 1);


            final int x = mat1.cols();
            int y = mat1.rows();
            int i, per;
            int count;
            int r = 0;
            double[] pixel = new double[1];
            y /= 2;


//brown
            Mat brown1 = new Mat();
            Mat brown2 = new Mat();
            Mat brown = new Mat();
            Mat brownMc = new Mat();
            Mat brownMo = new Mat();
            int brownX1 = 0;
            int brownX2 = 0;
            int brownX3 = 0;

            Core.inRange(mathsv, new Scalar(4, 70, 40), new Scalar(11, 255, 210), brown1); //brown
            Core.inRange(mathsv, new Scalar(175, 70, 60), new Scalar(180, 210, 210), brown2); //brown2

            Core.bitwise_or(brown1, brown2, brown);
            brown1.release();
            brown2.release();

            Imgproc.morphologyEx(brown, brownMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(brownMc, brownMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            brown.release();
            brownMc.release();
            count = 0;

            for (i = 0; i < x; i++) {

                pixel = brownMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (brownX1 > 0) {
                            if (brownX2 > 0) {
                                brownX3 = i;
                                r = 0;
                                break;
                            } else {
                                brownX2 = i;
                                r = 0;
                            }
                        } else {
                            brownX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            brownMo.release();


//red
            Mat red1 = new Mat();
            Mat red2 = new Mat();
            Mat red = new Mat();
            Mat redMc = new Mat();
            Mat redMo = new Mat();
            int redX1 = 0;
            int redX2 = 0;
            int redX3 = 0;

            Core.inRange(mathsv, new Scalar(0, 130, 150), new Scalar(7, 255, 255), red1); //red1
            Core.inRange(mathsv, new Scalar(175, 130, 150), new Scalar(180, 255, 255), red2); //red2

            Core.bitwise_or(red1, red2, red);
            red1.release();
            red2.release();

            Imgproc.morphologyEx(red, redMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(redMc, redMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            red.release();
            redMc.release();

            count = 0;

            for (i = 0; i < x; i++) {

                pixel = redMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (redX1 > 0) {
                            if (redX2 > 0) {
                                redX3 = i;
                                r = 0;
                                break;
                            } else {
                                redX2 = i;
                                r = 0;
                            }
                        } else {
                            redX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            redMo.release();


//orange
            Mat orange = new Mat();
            Mat orangeMc = new Mat();
            Mat orangeMo = new Mat();
            int orangeX1 = 0;
            int orangeX2 = 0;
            int orangeX3 = 0;

            Core.inRange(mathsv, new Scalar(8, 120, 140), new Scalar(10, 230, 230), orange); //orange

            Imgproc.morphologyEx(orange, orangeMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(orangeMc, orangeMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            orange.release();
            orangeMc.release();

            count = 0;

            for (i = 0; i < x; i++) {

                pixel = orangeMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (orangeX1 > 0) {
                            if (orangeX2 > 0) {
                                orangeX3 = i;
                                r = 0;
                                break;
                            } else {
                                orangeX2 = i;
                                r = 0;
                            }
                        } else {
                            orangeX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            orangeMo.release();


//black
            Mat black = new Mat();
            Mat blackMc = new Mat();
            Mat blackMo = new Mat();
            int blackX1 = 0;
            int blackX2 = 0;
            int blackX3 = 0;

            Core.inRange(mathsv, new Scalar(0, 100, 0), new Scalar(179, 255, 150), black); //black

            Imgproc.morphologyEx(black, blackMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(blackMc, blackMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            black.release();
            blackMc.release();

            count = 0;

            for (i = 0; i < x; i++) {

                pixel = blackMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (blackX1 > 0) {
                            if (blackX2 > 0) {
                                blackX3 = i;
                                r = 0;
                                break;
                            } else {
                                blackX2 = i;
                                r = 0;
                            }
                        } else {
                            blackX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            blackMo.release();


//yellow
            Mat yellow = new Mat();
            Mat yellowMc = new Mat();
            Mat yellowMo = new Mat();
            int yellowX1 = 0;
            int yellowX2 = 0;
            int yellowX3 = 0;

            Core.inRange(mathsv, new Scalar(16, 110, 150), new Scalar(25, 255, 255), yellow); //yellow

            Imgproc.morphologyEx(yellow, yellowMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(yellowMc, yellowMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            yellow.release();
            yellowMc.release();

            count = 0;

            for (i = 0; i < x; i++) {

                pixel = yellowMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (yellowX1 > 0) {
                            if (yellowX2 > 0) {
                                yellowX3 = i;
                                r = 0;
                                break;
                            } else {
                                yellowX2 = i;
                                r = 0;
                            }
                        } else {
                            yellowX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            yellowMo.release();





//blue

            Mat blue = new Mat();
            Mat blueMc = new Mat();
            Mat blueMo = new Mat();
            int blueX1 = 0;
            int blueX2 = 0;
            int blueX3 = 0;

            Core.inRange(mathsv, new Scalar(90, 70, 50), new Scalar(110, 255, 255), blue); //blue

            Imgproc.morphologyEx(blue, blueMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(blueMc, blueMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            blue.release();
            blueMc.release();

            count = 0;

            for (i = 0; i < x; i++) {

                pixel = blueMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (blueX1 > 0) {
                            if (blueX2 > 0) {
                                blueX3 = i;
                                r = 0;
                                break;
                            } else {
                                blueX2 = i;
                                r = 0;
                            }
                        } else {
                            blueX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            blueMo.release();







//purple

            Mat purple = new Mat();
            Mat purpleMc = new Mat();
            Mat purpleMo = new Mat();
            int purpleX1 = 0;
            int purpleX2 = 0;
            int purpleX3 = 0;

            Core.inRange(mathsv, new Scalar(125, 40, 50), new Scalar(150, 255, 255), purple); //purple

            Imgproc.morphologyEx(purple, purpleMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(purpleMc, purpleMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            purple.release();
            purpleMc.release();

            count = 0;

            for (i = 0; i < x; i++) {

                pixel = purpleMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (purpleX1 > 0) {
                            if (purpleX2 > 0) {
                                purpleX3 = i;
                                r = 0;
                                break;
                            } else {
                                purpleX2 = i;
                                r = 0;
                            }
                        } else {
                            purpleX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            //purpleMo.release();


//gray

            Mat gray = new Mat();
            Mat grayMc = new Mat();
            Mat grayMo = new Mat();
            int grayX1 = 0;
            int grayX2 = 0;
            int grayX3 = 0;

            Core.inRange(mathsv, new Scalar(5, 10, 100), new Scalar(13, 55, 255), gray); //gray

            Imgproc.morphologyEx(gray, grayMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(grayMc, grayMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            gray.release();
            grayMc.release();

            count = 0;

            for (i = 0; i < x; i++) {

                pixel = grayMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (grayX1 > 0) {
                            if (grayX2 > 0) {
                                grayX3 = i;
                                r = 0;
                                break;
                            } else {
                                grayX2 = i;
                                r = 0;
                            }
                        } else {
                            grayX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            grayMo.release();


//white

            Mat white = new Mat();
            Mat whiteMc = new Mat();
            Mat whiteMo = new Mat();
            int whiteX1 = 0;
            int whiteX2 = 0;
            int whiteX3 = 0;

            Core.inRange(mathsv, new Scalar(10, 30, 120), new Scalar(30, 90, 250), white); //white

            Imgproc.morphologyEx(white, whiteMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(whiteMc, whiteMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            white.release();
            whiteMc.release();

            count = 0;

            for (i = 0; i < x; i++) {

                pixel = whiteMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (whiteX1 > 0) {
                            if (whiteX2 > 0) {
                                whiteX3 = i;
                                r = 0;
                                break;
                            } else {
                                whiteX2 = i;
                                r = 0;
                            }
                        } else {
                            whiteX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            whiteMo.release();


//green

            Mat green = new Mat();
            Mat greenMc = new Mat();
            Mat greenMo = new Mat();
            int greenX1 = 0;
            int greenX2 = 0;
            int greenX3 = 0;

            Core.inRange(mathsv, new Scalar(30, 25, 10), new Scalar(85, 180, 210), green); //green

            Imgproc.morphologyEx(green, greenMc,
                    Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 5
            );

            Imgproc.morphologyEx(greenMc, greenMo,
                    Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(
                            Imgproc.MORPH_RECT, new Size(2, 5)
                    ),
                    new Point(-1, -1), 18
            );

            green.release();
            greenMc.release();

            count = 0;

            for (i = 0; i < x; i++) {

                pixel = greenMo.get(y - 1, i);     // y,x

                if (pixel[0] == 255.0) {
                    count = count + 1;
                    r = r + 1;
                } else {
                    count = count + 0;

                    per = r * 100 / x;

                    if (per >= 5 && r > 0) {
                        if (whiteX1 > 0) {
                            if (greenX2 > 0) {
                                greenX3 = i;
                                r = 0;
                                break;
                            } else {
                                greenX2 = i;
                                r = 0;
                            }
                        } else {
                            greenX1 = i;
                            r = 0;
                        }
                    } else {
                        r = 0;
                    }
                }

            }

            greenMo.release();







//Resistance value

            int colorV[] = new int[30];

            colorV[0] = brownX1;
            colorV[1] = brownX2;
            colorV[2] = brownX3;
            colorV[3] = redX1;
            colorV[4] = redX2;
            colorV[5] = redX3;
            colorV[6] = orangeX1;
            colorV[7] = orangeX2;
            colorV[8] = orangeX3;
            colorV[9] = yellowX1;
            colorV[10] = yellowX2;
            colorV[11] = yellowX3;
            colorV[12] = greenX1;
            colorV[13] = greenX2;
            colorV[14] = greenX3;
            colorV[15] = blueX1;
            colorV[16] = blueX2;
            colorV[17] = blueX3;
            colorV[18] = purpleX1;
            colorV[19] = purpleX2;
            colorV[20] = purpleX3;
            colorV[21] = grayX1;
            colorV[22] = grayX2;
            colorV[23] = grayX3;
            colorV[24] = whiteX1;
            colorV[25] = whiteX2;
            colorV[26] = whiteX3;
            colorV[27] = blackX1;
            colorV[28] = blackX2;
            colorV[29] = blackX3;


            int colorR[] = new int[3];

            for (i=0;i<30;i++){
                if (colorV[i]>0 && colorR[0]==0){
                    colorR[0]=colorV[i];
                    continue;
                }
                if (colorV[i]>0 && colorR[0]>0 && colorR[1]==0){
                    colorR[1]=colorV[i];
                    continue;
                }
                if (colorV[i]>0 && colorR[0]>0 && colorR[1]>0 && colorR[2]==0){
                    colorR[2]=colorV[i];
                    break;
                }
            }


            int temp;
            int j;


            for(i=0; i<2; i++){
                for( j=0; j<2; j++){
                    if(colorR[j]>colorR[j+1] && colorR[j+1]>0){
                        temp=colorR[j];
                        colorR[j]=colorR[j+1];
                        colorR[j+1]=temp;
                    }
                }
            }


            int r1=0;
            int r2=0;
            int r3=1;


            if (colorR[0]==brownX1 || colorR[0]==brownX2 || colorR[0]==brownX3){
                r1 = 10;
            }

            if (colorR[1]==brownX1 || colorR[1]==brownX2 || colorR[1]==brownX3){
                r2 = 1;
            }

            if (colorR[2]==brownX1 || colorR[2]==brownX2 || colorR[2]==brownX3){
                r3 = (int)Math.pow(10,1);
            }




            if (colorR[0]==redX1 || colorR[0]==redX2 || colorR[0]==redX3){
                r1 = 20;
            }

            if (colorR[1]==redX1 || colorR[1]==redX2 || colorR[1]==redX3){
                r2 = 2;
            }

            if (colorR[2]==redX1 || colorR[2]==redX2 || colorR[2]==redX3){
                r3 = (int)Math.pow(10,2);
            }





            if (colorR[0]==orangeX1 || colorR[0]==orangeX2 || colorR[0]==orangeX3){
                r1 = 30;
            }

            if (colorR[1]==orangeX1 || colorR[1]==orangeX2 || colorR[1]==orangeX3){
                r2 = 3;
            }

            if (colorR[2]==orangeX1 || colorR[2]==orangeX2 || colorR[2]==orangeX3){
                r3 = (int)Math.pow(10,3);
            }






            if (colorR[0]==yellowX1 || colorR[0]==yellowX2 || colorR[0]==yellowX3){
                r1 = 40;
            }

            if (colorR[1]==yellowX1 || colorR[1]==yellowX2 || colorR[1]==yellowX3){
                r2 = 4;
            }

            if (colorR[2]==yellowX1 || colorR[2]==yellowX2 || colorR[2]==yellowX3){
                r3 = (int)Math.pow(10,4);
            }





            if (colorR[0]==greenX1 || colorR[0]==greenX2 || colorR[0]==greenX3){
                r1 = 50;
            }

            if (colorR[1]==greenX1 || colorR[1]==greenX2 || colorR[1]==greenX3){
                r2 = 5;
            }

            if (colorR[2]==greenX1 || colorR[2]==greenX2 || colorR[2]==greenX3){
                r3 = (int)Math.pow(10,5);
            }






            if (colorR[0]==blueX1 || colorR[0]==blueX2 || colorR[0]==blueX3){
                r1 = 60;
            }

            if (colorR[1]==blueX1 || colorR[1]==blueX2 || colorR[1]==blueX3){
                r2 = 6;
            }

            if (colorR[2]==blueX1 || colorR[2]==blueX2 || colorR[2]==blueX3){
                r3 = (int)Math.pow(10,6);
            }





            if (colorR[0]==purpleX1 || colorR[0]==purpleX2 || colorR[0]==purpleX3){
                r1 = 70;
            }

            if (colorR[1]==purpleX1 || colorR[1]==purpleX2 || colorR[1]==purpleX3){
                r2 = 7;
            }

            if (colorR[2]==purpleX1 || colorR[2]==purpleX2 || colorR[2]==purpleX3){
                r3 = (int)Math.pow(10,7);
            }






            if (colorR[0]==grayX1 || colorR[0]==grayX2 || colorR[0]==grayX3){
                r1 = 80;
            }

            if (colorR[1]==grayX1 || colorR[1]==grayX2 || colorR[1]==grayX3){
                r2 = 8;
            }

            if (colorR[2]==grayX1 || colorR[2]==grayX2 || colorR[2]==grayX3){
                r3 = (int)Math.pow(10,8);
            }





            if (colorR[0]==whiteX1 || colorR[0]==whiteX2 || colorR[0]==whiteX3){
                r1 = 90;
            }

            if (colorR[1]==whiteX1 || colorR[1]==whiteX2 || colorR[1]==whiteX3){
                r2 = 9;
            }

            if (colorR[2]==whiteX1 || colorR[2]==whiteX2 || colorR[2]==whiteX3){
                r3 = (int)Math.pow(10,9);
            }






            if (colorR[1]==blackX1 || colorR[1]==blackX2 || colorR[1]==blackX3){
                r2 = 0;
            }

            if (colorR[2]==blackX1 || colorR[2]==blackX2 || colorR[2]==blackX3){
                r3 = 1;
            }











            rv.setText(String.valueOf( (r1 + r2)*r3 ));



            //double[] pValue = molfd.get(y, x);
            //rv.setText(String.valueOf(pValue[0]));

            //Size sizeM = molfd.size();
            //double[] data = molfd.get(100,200);
            //rv.setText(String.valueOf(data[0]));

            //double[] data = new double[1];
            //data = molfd.get(y-1, x-1);     // y,x
            //rv.setText(String.valueOf(data[0]));


            //Bitmap bmp2 = convGray2BMP(purpleMo);
            // member methods



            mButton.setVisibility(View.INVISIBLE);

            ((ImageView) findViewById(R.id.imageView)).setImageBitmap(bitmap);




        }

    }


    // member methods
    private Bitmap convGray2BMP(Mat srcData) {
        Mat mat4 = new Mat();
        Mat mat5 = new Mat();

        Imgproc.cvtColor(srcData, mat4, Imgproc.COLOR_GRAY2BGR, 0);
        Imgproc.cvtColor(mat4, mat5, Imgproc.COLOR_BGR2RGBA, 0);

        //Bitmap bmp2 = Bitmap.createBitmap(mat5.width(), mat5.height(), Bitmap.Config.ARGB_8888);
        Bitmap bmp2 = Bitmap.createBitmap(mat5.cols(), mat5.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat5, bmp2);
        return bmp2;
    }





/*
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("TouchEvent", "X:" + event.getX() + ",Y:" + event.getY());
        return true;
    }
*/





    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume called.");
        mButton.setEnabled(false);
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, mLoaderCallback);
    }





    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.adm.color/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.adm.color/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


