# MOCAPocket

<p align="center">
<img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/main.png" style="height: 100%; width:100%;"/></center></a></p>

Android App for measure lower body kinematics with [Movella Dot](https://www.movella.com/products/wearables/movella-dot) sensors.

## Requirements

- Android version 12.0 or higher.
- Minimum sensors firmware version 3.0.0.

---

## Instructions

<table>
      <tr>
          <th columnspan=2> First screen </th>
      </tr>
      <tr>
          <th><img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/first.jpg" style="height: 75%; width:75%;"/></th>
          <th><p align="justify"> Open MOCAPocket App with the Bluetooth enabled. <br><br> 
            Movella sensors that are turned on are automatically connected and their output rate and filter are set to 30 Hz and General, respectively. These parameters can be changed in the Configuration Options ⚙️.                 <br><br> All connected sensors can be turned off: More -> Turn off. <br><br> Tap on sensor´s card to change their names and to match them with the lower body segments. 
            Sensors can be identified tapping on the green stars :eight_spoked_asterisk:. </p>
          </th>
      </tr>
      <tr>
          <th columnspan=2> Second screen </th>
      </tr>
      <tr>
          <th><img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/second.jpg" style="height: 75%; width:75%;"/></th>
          <th><p align="justify"> Once seven sensors connected, go to the second screen, tap on Edit ✏️ and enter a recording file name and the subject height. While not recording, filename can be change at any time, but                 subject height can only be modified before calibration. <br><br> Next, press on Start to calibrate the model. You can choose calibrate sensors before the model. 
                Calibrating sensors just check drift in sensors orientation, so it is recommended to get more reliable kinematic measurements. <br><br> Model calibration has two phases. First, static calibration (5                         seconds). Second, dynamic calibration (10 seconds). To get better dynamic calibration results, subject must walk straight. </p>
          </th>
      </tr>
      <tr>
          <th columnspan=2> Third screen </th>
      </tr>
      <tr>
          <th><img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/third.jpg" style="height: 75%; width:75%;"/></th>
          <th><p align="justify"> On the third screen you can plot joint angles and segment orientations, as well as start and stop recordings. While recording, a red rectangle will appear around the chart.  <br><br> 
                Recordings are saved as a CSV file in: <br> /storage/emulated/0/Android/data/com.bioapp.mocappocket/files/recordings.  <br><br> 
                The format of the recording´s names are: filename + _yyyy_MM_dd_HH_mm_ss. </p>
          </th>
      </tr>
      <tr>
          <th columnspan=2> Fourth screen </th>
      </tr>
      <th><img src="https://github.com/JuanCS96/MOCAPocket/blob/main/images/fourth.jpg" style="height: 75%; width:75%;"/></th>
      <th><p align="justify"> Fourth screen shows the simulation of the user motion on a skeletal model. You can modify camera view as follow: <br> - Slide horizontaly or verticaly with one finger to rotate the view                   around the model. <br> - With two fingers pinch to zoom in and spread to zoom out. <br> -  Two fingers pan to move the model horizontaly and verticaly. <br><br> 
            Press on the top left button to go back and on the right one to restart the view. </p>
      </th>
</table>

---
