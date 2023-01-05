import io
import cv2
import numpy as np
import face_recognition
import base64
from PIL import Image

def display(name):
    return 'hello ' + name

def getImage(byteArray1, byteArray2):
    # pic = Image.open(io.BytesIO(bytes(byteArray)))
    imgBill = face_recognition.load_image_file(io.BytesIO(bytes(byteArray1)))
    imgBill = cv2.cvtColor(imgBill,cv2.COLOR_BGR2RGB)
    imgBill1 = face_recognition.load_image_file(io.BytesIO(bytes(byteArray2)))
    imgBill1 = cv2.cvtColor(imgBill1,cv2.COLOR_BGR2RGB)

    faceLoc = face_recognition.face_locations(imgBill)[0] # Finds the approx location of the face
    encodeBill = face_recognition.face_encodings(imgBill)[0] # Encodes the face location. Used for comparing if two image  are identical or not.
    cv2.rectangle(imgBill,(faceLoc[3],faceLoc[0]),(faceLoc[1],faceLoc[2]),(255,0,255),2)
    # print(faceLoc)

    faceLocTest = face_recognition.face_locations(imgBill1)[0]
    encodeBillTest = face_recognition.face_encodings(imgBill1)[0]
    cv2.rectangle(imgBill1,(faceLocTest[3],faceLocTest[0]),(faceLocTest[1],faceLocTest[2]),(255,0,255),2) # Draws a rectangle around face
    results = face_recognition.compare_faces([encodeBill],encodeBillTest) # Returns true if images are identical
    faceDis = face_recognition.face_distance([encodeBill],encodeBillTest) # Checks how identical two images are.
    if results:
        return "identical"
    else:
        return "not identical"

def recognize(data):
    decoded_data = base64.b64decode(data)
    np_data = np.fromstring(decoded_data,np.uint8)
    img = cv2.imdecode(np_data,cv2.IMREAD_UNCHANGED)

    img_rgb = cv2.cvtColor(img,cv2.COLOR_BGR2RGB)
    img_gray = cv2.cvtColor(img,cv2.COLOR_BGR2GRAY)

    face_locations = face_recognition.face_locations(img_gray)
    for(top,right,bottom,left) in face_locations:
        cv2.rectangle(img_rgb,(left,top),(right,bottom),(0,0,255),8)



    pil_im = Image.fromarray(img_rgb)
    buff = io.BytesIO()
    pil_im.save(buff,format="PNG")
    img_str = base64.b64encode(buff.getvalue())
    return ""+str(img_str,'utf-8')
