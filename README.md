# IMUcap

<p align="center">
<img src="https://github.com/JuanCS96/IMUcap/blob/main/images/main.png" style="height: 100%; width:100%;"/></center></a></p>

[Android App](https://play.google.com/store/apps/details?id=com.biomechApp.imuCap) to measure joint angles and segment orientations with [Movella Dot](https://www.movella.com/products/wearables/movella-dot) sensors.

https://github.com/user-attachments/assets/ec480f13-fbca-42aa-8d2e-3d0821731583

https://github.com/user-attachments/assets/e6cddad9-966b-44e0-8227-264eb03617d1

## Requirements

- Android version 12.0 or higher.
- Minimum sensors firmware version 3.0.0.
- Two Movella Dot sensors at least.

---

### First screen

<div>
    <p><br> Open IMUcap App with Bluetooth enabled. Movella sensors that are turned on will connect automatically. Sensors can be identified tapping on the green stars :eight_spoked_asterisk:. <br> </p>
    <img src="https://github.com/JuanCS96/IMUcap/blob/main/images/first.jpg" style="height: 30%; width:30%;"/>
    <p><br>  By default, their output rate and filter are set to 30 Hz and General, respectively. These parameters can be changed in Sensor Settings ⚙️. <br> <br> </p>             
    <img src="https://github.com/JuanCS96/IMUcap/blob/main/images/settings.jpg" style="height: 30%; width:30%;"/> 
    <p><br> Tap on sensor´s card to change their names and to match them with the body segments. </p>
    <img src="https://github.com/JuanCS96/IMUcap/blob/main/images/config.jpg" style="height: 30%; width:30%;"/> 
    <p><br> All connected sensors can be turned off: More -> Turn off. </p>
</div>

---

### Second screen
<div>
    <p><br> On the second screen, tap on Edit ✏️ and enter a recording filename. While not recording, filename can be changed at         any time. <br></p>
    <p><br> Select the desired joints to measure. Up to six joints can be choosen. Note that the selected joints must match the segments defined in the sensor configuration. <br></p>
    <img src="https://github.com/JuanCS96/IMUcap/blob/main/images/second.jpg" style="height: 30%; width:30%;"/>
    <p><br> Press on Start and follow instructions. A sensor calibration will be performed first. Place the sensors together on a flat surface with the Z axes poitting up and the X axes heading in the same direction. </p>
    <p><br> Next, put the sensors on the corresponding user segments and hold the static pose for 5 seconds. Then, bend joints following instructions. <br></p>
    <img src="https://github.com/JuanCS96/IMUcap/blob/main/images/calib.png" style="height: 80%; width:80%;"/>
</div>

---

### Third screen
<div>
    <p><br> On the third screen, live joint angles and segment orientations are plotted. Up to four signals can be plotted at one. Press the second top-right button to open the graph menu. During recording, a red rectangle appears around the graph. <br></p>
    <img src="https://github.com/JuanCS96/IMUcap/blob/main/images/third.jpg" style="height: 30%; width:30%;"/>
    <p><br> The first top-right button shows the simulation of the user motion on a skeletal model in real-time. The simulation only works when the right and left hip, knee and ankle joints are selected. <br></p>
    <img src="https://github.com/JuanCS96/IMUcap/blob/main/images/model.jpg" style="height: 40%; width:40%;"/>
    <p><br><br> Camera view can be modify as follow: <br> - Slide horizontaly or verticaly with one finger to rotate the view around the model. <br> - With two fingers pinch to zoom in and spread to zoom out. <br> -  Two 
    fingers pan to move the model horizontaly and verticaly. <br> -  Top right button resets the view. Top left button goes back. </p>

                
</div>

---

### Fourth screen
<div>
    <p><br> On the fourth screen, you can start and stop recordings. The top-right button manages recordings without video. To record data and video simultaneously, use the camera button. <br></p>
    <img src="https://github.com/JuanCS96/IMUcap/blob/main/images/fourth.png" style="height: 30%; width:30%;"/>
    <p><br> Recordings are saved as a .csv files in /storage/emulated/0/Android/data/com.biomechApp.imuCap/files/recordings with the       name of: filename + _yyyy_MM_dd_HH_mm_ss. <br></p>
    <img src="https://github.com/JuanCS96/IMUcap/blob/main/images/trial.jpg" style="height: 30%; width:30%;"/>
    <p><br> Videos are saved as a .mp4 files in /storage/emulated/0/Android/data/com.biomechApp.imuCap/files/Movies with the               name of: filename + _yyyy_MM_dd_HH_mm_ss. <br></p>
</div>

---

