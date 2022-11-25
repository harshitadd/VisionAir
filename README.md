# Celestini Program India 2019 Repository 

This rep contains all the codes used in VisionAir's Deep Learning model and all the adjoining ML datastructures which were used to finalise the configurations of the final model. 

Script Information: 
- CRF: Implements the Camera Response Function used to estimate the non-linear transformation on the raw information captured by edge device' camera. 
- Graphs: Quantized, Non-Quantized offline models of all the cascased RNN + DNN. 
- Scripts: Functionalities provided by the scripts are specified here
      - Crawling CPCB for real-time air quality data
      
      - regression archictures for RNN, DNN, CNN and Random Forest type of the model. (We deploy the tflite variant of the RF, Others are for benchmarking) 
      - DNN inference
      
      - Keras and Non-Keras model definitions
      
      - Sensor callibration 
      
      - Converting LDR to HDR using LDR Fusion
      
      - Scripts for computing feature constants: Omega, Flattened Haze 

#### Please note that this repository is not being actively managed currently.

![VisionAIR (1)](https://user-images.githubusercontent.com/31439716/80715263-baa16400-8b13-11ea-9bc7-0fd5d9b303a8.png)
