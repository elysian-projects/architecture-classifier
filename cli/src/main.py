import numpy as np
import tensorflow as tf
from utils import *

CLASS_NAMES = ["Class 1", "Class 2", "Class 3", "Class 4", "Class 5"]

interpreter = tf.lite.Interpreter(model_path = resolve_model_path("flowers"))
interpreter.allocate_tensors()

input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

input_shape = input_details[0]['shape']

images = get_images_paths()
image = resize_image(read_image(images[0]), 180, 180)

input_data = np.array([image], dtype = np.float32)

print(input_data)

interpreter.set_tensor(input_details[0]['index'], input_data)

interpreter.invoke()

output_data = interpreter.get_tensor(output_details[0]['index'])
print(output_data)