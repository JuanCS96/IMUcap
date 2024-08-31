# IMUcap

<p align="center">
<img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/main.png" style="height: 100%; width:100%;"/></center></a></p>

Android App for measure lower body kinematics with [Movella Dot](https://www.movella.com/products/wearables/movella-dot) sensors.

## Requirements

- Android version 12.0 or higher.
- Minimum sensors firmware version 3.0.0.

---

### First screen

<div>
    <p><br> Open IMUcap App with Bluetooth enabled. Movella sensors that are turned on will connect automatically. <br> </p>
    <img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/first.jpg" style="height: 30%; width:30%;"/>
    <p><br>  By default, their output rate and filter are set to 30 Hz and General, respectively. These parameters can be changed in Sensor Settings ⚙️. <br> <br> </p>             
    <img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/settings.jpg" style="height: 30%; width:30%;"/> 
    <p><br> Tap on sensor´s card to change their names and to match them with the lower body segments. Sensors can be identified tapping on the green stars :eight_spoked_asterisk:. </p>
    <img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/config.jpg" style="height: 30%; width:30%;"/> 
    <p><br> All connected sensors can be turned off: More -> Turn off. </p>
</div>

---

### Second screen
<div>
    <p><br> In the second screen, tap on Edit ✏️ and enter a recording file name and the user height. While not recording, filename can be changed at any time, but user height can only be modified before                      calibration. <br></p>
    <img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/second.jpg" style="height: 30%; width:30%;"/>
    <p><br> Press on Start and follow instructions. A sensor calibration will be performed first. To avoid sensor orientation drift, it is recommended to put the sensors together on a flat surface with their Z axis             poitting up. </p>
    <p><br> Next, put the sensors on the corresponding user segments and hold the static pose for 5 seconds. Then, bend knees and hips. <br></p>
    <img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/calib.jpg" style="height: 50%; width:50%;"/>
</div>

---

### Third screen
<div>
    <p><br> In the third screen, live joint angles and segment orientations are plotted (first top right button). Up to four signals can be plotted at one.<br></p>
    <img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/third.jpg" style="height: 30%; width:30%;"/>
    <p><br> Recordings start and stop pressing the second top right button. While recording, a red rectangle will appear around the chart. <br></p>
    <img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/recording.jpg" style="height: 30%; width:30%;"/>
    <p><br> Recordings are saved as a CSV file in /storage/emulated/0/Android/data.... with the name of: filename + _yyyy_MM_dd_HH_mm_ss. <br></p> 
    <img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/trial.jpg" style="height: 30%; width:30%;"/>
                
</div>

---

### Fourth screen
<div>
    <img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/fourth.jpg" style="height: 40%; width:40%;"/>
    <p><br><br> Fourth screen shows the simulation of the user motion on a skeletal model in real-time. Camera view can be modify as follow: <br> - Slide horizontaly or verticaly with one finger to rotate the view            around the model. <br> - With two fingers pinch to zoom in and spread to zoom out. <br> -  Two fingers pan to move the model horizontaly and verticaly.</p>
</div>

---
