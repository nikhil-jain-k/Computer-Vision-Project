# Project Description
MarketAI.exe allows you to detect and recognize faces, texts, and objects. User can either use the local computer or an Arduino module to input data. It supports real-time face detection, face recognition and object recognition. It is built using Java on the Spring Boot framework with the help of OpenCV (4.5.1) library. This team project, consisting of two members, was undertaken as part of an internship with RoyaltyBusayo.

# Prerequisites
1. Minimum version of JRE required : 1.7
2. Windows 10 

# Installation Instructions
1. Copy the MarketAI folder in C:\Program Files\
2. Run the exe file from "MarketAI\exe\MarketAI_v1.exe"
3. To connect to an Arduino Camera Module (OV7670):
   3.1 Connect the PC with port: COM9
   3.2 To find out what USB port your Arduino is connected to
       you could check it on your Control Panel >..
       > Device Manager > Ports (COM & LPT) > right click >.. 
       ..Properties > Port Settings > Advanced.. 
       > COM Port Number > /Select Your Port Number/
   3.3 In MarketAI.exe, select: File > Read Image from Arduino
   
# Features
## Detect Faces ##
After uploading an image, the user can select ***Detect Faces*** option from the ***Features*** tab in the GUI. An image would be displayed highlighting the faces in the image with the no. of faces detected.

## Detect Objects ##
The user can select ***Detect Objects*** option from the ***Features*** tab. An image would be displayed highlighting the objects and their names.

## Detect Text ##
After uploading an image, the user can select ***Detect Text*** option from the ***Features*** tab in the GUI. An image would be displayed highlighting the text in the image with the no. of words detected.

## Run OCR ## 
In addition to text detection, the OCR feature extracts text from the image.

## Real Time Face Recognition ##
The application can also recognize faces, once they have been saved in the database.

## Real Time Object Detection ##
Similar to ***Detect Objects***; this feature does it in real time.
