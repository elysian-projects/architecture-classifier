from os import path as __path
from os import listdir as __listdir
from os import environ as __environ
from PIL import Image
import numpy as np

__MODEL_BASE_DIR = "".join(__path.join(__path.dirname(__file__), "../models"))
__IMAGE_BASE_DIR = "".join(__path.join(__path.dirname(__file__), "../images"))
__CLASS_NAMES_PATH = "".join(__path.join(__path.dirname(__file__), "../classes.txt"))

__MODEL_EXTENSION = "tflite"
__IMAGE_EXTENSIONS = ["png", "jpg", "jpeg"]


def dataset_exists(filename):
  return __path.exists(__path.join(__MODEL_BASE_DIR, get_model_fullname(filename)))


def read_class_names():
  with open(__CLASS_NAMES_PATH, "r") as file:
    return file.read().split("\n")


def resolve_model_path(model_name: str) -> str:
  '''
  Resolves the model path by name. Looking for the model in the /cli/models folder
  NOTE: the name should be passed WITHOUT the extension
  '''
  return __path.join(__MODEL_BASE_DIR, get_model_fullname(model_name))


def get_model_fullname(model_name: str) -> str:
  return model_name + "." + __MODEL_EXTENSION


def read_image(image_path) -> Image:
  return Image.open(image_path).convert("RGB")


def resize_image(image, width, height) -> None:
  temp_image = image.resize([width, height])
  return np.array(temp_image)


def get_images_paths() -> list[str]:
  '''
  Returns a list of paths to images, located in the /cli/images folder
  '''
  return [
    __path.join(__IMAGE_BASE_DIR, file)
      for file in __listdir(__IMAGE_BASE_DIR) if __is_image_file(file)
  ]


def __is_image_file(filename: str) -> bool:
  return __path.isfile(__path.join(__IMAGE_BASE_DIR, filename)) and __is_available_image_extension(filename)


def __is_available_image_extension(file: str) -> bool:
  return file.split(".")[-1] in __IMAGE_EXTENSIONS


def get_result(model_output: list[float], classes: list[str]) -> str:
  validate_output(model_output, classes)

  max_value = -10000
  max_index = 0

  for i in range(len(model_output)):
    if(model_output[i] > max_value):
      max_value = model_output[i]
      max_index = i

  return classes[max_index]


def print_result(result: str, filename: str) -> None:
  normalized_filename = __path.normpath(filename)
  separator = "/" if "/" in normalized_filename else "\\"
  file_short_name = normalized_filename.split(separator)[-1]

  print(f"{file_short_name}: {result}")


def validate_output(model_output: list[float], classes: list[str]):
  if(len(model_output) != len(classes)):
    print(len(model_output), len(classes))
    print("Model output length does not match the number of classes!")
    exit(1)


def check_if_model_provided(args):
  if(len(args) == 0):
    print("The model name must be provided!")
    exit(1)

  if(not dataset_exists(args[0])):
    print(f"The model `{args[0]}.tflite` does not exist in the models directory!")
    exit(1)


def disable_tensorflow_debugging_info_if_required(args):
  if(not "--show-tf-debug" in args):
    __environ["TF_CPP_MIN_LOG_LEVEL"] = "3"
