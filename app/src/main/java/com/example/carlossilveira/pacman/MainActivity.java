package com.example.carlossilveira.pacman;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    TextView txtC;
    SensorManager sensorManager;
    Sensor sensorAccelerometer;
    int screenWidth=0, screenHeight=0,puntaje=0,cuadros=0;
    ImageButton btnMain;
    Button[] botones;
    ConstraintLayout activity_main;

    float x,y,z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtC = findViewById(R.id.txtC);
        btnMain= findViewById(R.id.btnMain);
        activity_main=findViewById(R.id.activity_main);
        Display display=getWindowManager().getDefaultDisplay();
        Point p=new Point();
        display.getSize(p);
        screenWidth=p.x;
        screenHeight=p.y;
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        Random r=new Random();
        cuadros=r.nextInt(20-6)+6;
        generar();
    }
    public void generar(){
        int x=screenWidth/cuadros;
        int y=screenHeight/cuadros;
        boolean c=false;
        botones=new Button[cuadros*cuadros];
        int cont=0;
        for(int j=0;j<cuadros;j++){
            for (int k=0;k<cuadros;k++){
                Button b=new Button(getApplicationContext());
                b.setLayoutParams(new LinearLayout.LayoutParams(x,y));
                b.setText("*");
                b.setX(k*x);
                b.setY(j*x);
                if(c){
                    b.setBackgroundColor(Color.parseColor("#25918d"));
                    c=false;
                }else{
                    b.setBackgroundColor(Color.parseColor("#0fbc1d"));
                    c=true;
                }
                activity_main.addView(b);
                botones[cont]=b;
                cont++;
            }
        }
    }

    long tAnterior;

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            long cTime = System.currentTimeMillis();
            if ((cTime - tAnterior) > 100) {
                txtC.setText("X: " + x + " Y: " + y + " Z: " + z);

                tAnterior = cTime;
                int centerX=screenWidth/2;
                int centerY=screenHeight/2;
                int rel=(screenWidth/20);
                int relY=(screenHeight/20);
                if(x>0){
                    int _x=(int) (centerX-(rel*(x*-1)-25));
                    btnMain.setX(_x);
                }else{
                    btnMain.setX((rel*x)-25+centerX);
                }
                if(y>0){
                    btnMain.setY((centerY- (relY*(y*-1)))-25);
                }else{
                    btnMain.setY((centerY+(relY*y))-25);
                }
                for(int a=0;a<(cuadros*cuadros);a++){
                    Button b=botones[a];
                    if(b.getVisibility()== View.VISIBLE){
                        int _x=(int) b.getX();
                        int _y=(int) b.getY();
                        int _w=screenWidth/cuadros;
                        int _h=screenHeight/cuadros;
                        int cOx=(int) btnMain.getX()+(btnMain.getWidth()/2);
                        int cOy=(int) btnMain.getY()+(btnMain.getHeight()/2);
                        if(cOx>= _x &&
                                cOx <= (_x+_w) &&
                                cOy >= _y &&
                                cOy <= _y+_h){
                            b.setVisibility(View.INVISIBLE);
                            puntaje++;
                            this.getSupportActionBar().setTitle("Puntaje"+puntaje);
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //detenemos el sensor cuando el app este en segundo plano
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorAccelerometer, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}