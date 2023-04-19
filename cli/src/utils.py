from os import path as __path
from os import listdir as __listdir
from PIL import Image
import imageio as __iio
import numpy as np

__MODEL_BASE_DIR = "".join(__path.join(__path.dirname(__file__), "../models"))
__IMAGE_BASE_DIR = "".join(__path.join(__path.dirname(__file__), "../images"))

__MODEL_EXTENSION = "tflite"
__IMAGE_EXTENSIONS = ["png", "jpg", "jpeg"]


def resolve_model_path(model_name: str) -> str:
  '''
  Resolves the model path by name. Looking for the model in the /cli/models folder
  NOTE: the name should be passed WITHOUT the extension
  '''
  return __path.join(__MODEL_BASE_DIR, model_name + "." + __MODEL_EXTENSION)


def read_image(image_path):
  return __iio.imread(image_path)


def resize_image(image, width, height):
  image = Image.fromarray(image)
  image.thumbnail([width, height])

  pix = np.array(image.getdata())

  data = list(tuple(pixel) for pixel in pix)
  image.putdata(data)

  return np.array(image)


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
