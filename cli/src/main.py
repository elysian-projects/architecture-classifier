import numpy as np
import sys
from utils import *

args = sys.argv[1:]

# The model must be passed explicitly as the first argument
check_if_model_provided(args)

MODEL_PATH = resolve_model_path(args[0])
CLASS_NAMES = read_class_names()
IMAGES = get_images_paths()

# This function helps disable default tensorflow debug messages,
# pass the `--show-tf-debug` flag to enable them
disable_tensorflow_debugging_info_if_required(args)

# Import the tensorflow module AFTER checking if the debug messages are enabled
import tensorflow as tf

interpreter = tf.lite.Interpreter(model_path = MODEL_PATH)
interpreter.allocate_tensors()

input_size = interpreter.get_input_details()[0]["shape"]

IMAGE_WIDTH = input_size[1]
IMAGE_HEIGHT = input_size[2]

for image in IMAGES:
  image_as_array = resize_image(read_image(image), IMAGE_WIDTH, IMAGE_HEIGHT)

  input_data = np.array(np.expand_dims(image_as_array, axis = 0), dtype = np.float32)

  interpreter.set_tensor(interpreter.get_input_details()[0]['index'], input_data)
  interpreter.invoke()

  output_data = interpreter.get_tensor(interpreter.get_output_details()[0]['index'])
  print(output_data[0])
  print_result(get_result(output_data[0], CLASS_NAMES), image)
