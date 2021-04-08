About
-----
XesiAI.exe allows you to detect and recognize faces, texts, and objects. User can either use the local computer or an 
Arduino module to input data. It supports real-time face detection, face recognition and object recognition. It is built
using Java on the Spring Boot framework with the help of OpenCV (4.5.1) library.

Prerequisites for XesiAI.exe
----------------------------
1. Minimum version of JRE: 1.7
2. OS: Windows 

Installation Instructions
-------------------------
1. Copy the XesiAI folder in C:\Program Files\
2. Run the exe file from "C:\Program Files\XesiAI\bin\exe\XesiAI.exe"
3. To connect to an Arduino Camera Module (OV7670):
   3.1 Connect the PC with port: COM9
   3.2 To find out what USB port your Arduino is connected to
       you could check it on your Control Panel >..
       > Device Manager > Ports (COM & LPT) > right click >.. 
       ..Properties > Port Settings > Advanced.. 
       > COM Port Number > /Select Your Port Number/
   3.3 In XesiAI.exe, select: File > Read Image from Arduino

Important Info
-------------------------
1. Using "Save Screen" button on the main screen, the user can save the current screen as an image.
The image would be stored in: "C:\Program Files\XesiAI\bin\exe\outputs\"

2. The user can connect to an IP camera for streaming by inputting the RSVP URL

3. It is recommended to always run the XesiAI.exe file from:
"C:\Program Files\XesiAI\bin\exe\XesiAI.exe"; However, a shortcut can be created and placed elsewhere.

Detailed Information of Features
--------------------------------

1. Detect Faces: After uploading an image, the user will select "Detect Faces" option from the features tab. Then, the
window will be displayed which will highlight the faces in the image and will display the no. of faces detected in the image.

2. Detect Objects: After uploading an image, the user will select "Detect Objects" option from the features tab. Then, the 
window will be displayed which will highlight the objects in the image.

3. Detect Text: After uploading an image,the user will select "Detect Text" option from the features tab. Then, the window
will be displayed which will highlight the text in the image and will display the no. of words detected in the image.

4. Run OCR: After uploading an image,the user will select "Run OCR" option from the features tab. Then, the window will 
be displayed which will recognize the text in the image and will display the content being recognized on another frame 
having console window. 

5. Real Time Face Recognition: After selecting the "Real Time Face Recognition" from the features tab, the web-camera
will be displayed which will highlight the face and will ask for the name of the user. When the user enters the name 
and saves it, the web camera will recognize the user every time the user would run the feature.

6. Real Time Object Detection: After selecting the "Real Time Object Detection" from the features tab, the web-camera 
will be displayed which will highlight the objects in the frame and will display the name of the object being detected.

7. Real Time Motion Tracking: The application is able to detect and track motion in real time, users can also choose to 
enable an alert when motion is detected.   