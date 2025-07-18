# DigitReader-AI

Digit Reader is an experimental platform for artificial intelligence, designed around the well-known MNIST dataset—a collection of thousands of hand-drawn digits commonly used for training and testing image recognition models. The core objective of Digit Reader is to explore a fully custom, modular concept implementation of a simple AI architecture. Instead of using pre-built tools, the system is constructed using a layered node-based approach, where each layer consists of interconnected nodes that process and propagate data forward through the network. By focusing on simplicity and modularity, Digit Reader provides a clear and customizable framework for understanding the fundamentals of neural networks, data flow, and training algorithms—particularly in the context of digit classification problems like those posed by the MNIST dataset.

## File Structure

The file structure of the project is generalized below. CSV data libraries containing gray-scale 28x28 images of handwritten digits used to train and test the models are stored under lib. Each CSV line contains the expected digit followed by 784 values representing each pixel in the image. Communication with lib is handled by classes under data_interface. CSV information is read by the CSVReader which stores and processes the information in the extended class DigitToken.java (extended from parent class Token.java). Each token can be viewed at output.png via the DigitViewer.java class. Classes relating directly to the operation of the neural network can be found under neural_engine with training weights and biases stored under each network folder in network_data.

```
digit/
├── lib/
│   └── ... (CSV data libraries)
├── src/
|   ├── data_interface/
|   |   ├── CSVReader.java
|   |   ├── DigitToken.java
|   |   └── DigitViewer.java
|   ├── neural_engine/
|   |   ├── Layer.java
|   |   ├── Network.java
|   |   ├── Node.java
|   |   ├── Token.java
|   |   └── Trainer.java
|   └── network_data/
|       ├── small_model/
|       |   └── ...
|       └── large_model/
|           └── ...
├── App.java
└── ...
```

## Model Analysis
The modular neural network approach is flexible and efficient. Simply create a folder under network_data and instantiate a new Network object at the folder location. A config file and relevant data files will be created. Training data can be fed via tokens into the network via the trainer to automatically adjust weights and bias values. This repository already contains two example networks: small_model and large_model. small_model yields an accuracy of ~92% while large_model can achieve ~98% accuracy.