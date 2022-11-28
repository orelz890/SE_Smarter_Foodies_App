import requests
import re
from bs4 import BeautifulSoup as bs, BeautifulSoup
import json

from dish import dish

URLlist = []
objects = []


def URLScollect():
    # meat and chiken
    for i in range(1, 12):
        URL = 'https://www.10dakot.co.il/category/%D7%A2%D7%95%D7%A3-%D7%95%D7%91%D7%A9%D7%A8/?page=' + str(i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        for X in found:
            URLlist.append(X)
    # brakefast
    for i in range(1, 16):
        URL = 'https://www.10dakot.co.il/category/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%9C%D7%90%D7%A8%D7%95%D7%97%D7%AA-%D7%A2%D7%A8%D7%91/?page=' + str(
            i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        for X in found:
            URLlist.append(X)
    # helthy respis
    for i in range(1, 21):
        URL = 'https://www.10dakot.co.il/category/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%91%D7%A8%D7%99%D7%90%D7%99%D7%9D/?page=' + str(
            i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        for X in found:
            URLlist.append(X)
    # breds
    for i in range(1, 5):
        URL = 'https://www.10dakot.co.il/category/%d7%9e%d7%90%d7%a4%d7%99%d7%9d/?page=' + str(i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        for X in found:
            URLlist.append(X)
    # veigen
    for i in range(1, 32):
        URL = 'https://www.10dakot.co.il/category/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%98%D7%91%D7%A2%D7%95%D7%A0%D7%99%D7%99%D7%9D/?page=' + str(
            i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        for X in found:
            URLlist.append(X)


def date():

    for URL in URLlist:
        try:
            arry_photo = []
            r = requests.get(URL)
            soup = BeautifulSoup(r.content, 'html.parser')
            proudects = soup.findAll("div", {"class":"resipes__content"})[0].text
            x = proudects.find("רוצים")
            y = proudects.find("אופן ההכנה")
            z = proudects.find("טיפים")
            condumens = proudects[0:x:1]
            tips =""
            if(z == -1):
                make_way = proudects[y::1]
            else:
                make_way = proudects[y:z:1]
                tips = proudects[z::1]
            informison = soup.findAll("div", {"class": "resipes__header-col"})
            preparation_time = "זמן הכנה: "
            cooking_biking_time = "זמן בישול/אפיה: "
            num_of_Dishes = "מס' מנות: "
            category_type = "סוג קטגוריה: "
            for i in informison:
                if i.text.find("זמן הכנה") != -1 and i.text.find("זמן בישול") == -1 and i.text.find("מנות") == -1 and i.text.find("סוג") == -1:
                    preparation_time += i.text

                if i.text.find("זמן הכנה") == -1 and i.text.find("זמן בישול") != -1 and i.text.find("מנות") == -1 and i.text.find("סוג") == -1:
                    cooking_biking_time = i.text

                if (i.text.find("זמן הכנה") == -1 and i.text.find("זמן בישול") == -1 and i.text.find("מנות") != -1 and i.text.find("סוג") == -1):
                    num_of_Dishes = i.text

                if (i.text.find("זמן הכנה") == -1 and i.text.find("זמן בישול") == -1 and i.text.find("מנות") == -1 and i.text.find("סוג") != -1):
                    category_type = i.text

            try:
                category = soup.findAll("div", {"class": "banner_resipe__tag"})[0].text
            except Exception as e:
                category = category_type

            try:
                name = soup.findAll("h1", {"class": "banner_resipe__title"})[0].text

            except Exception as e:
                name = ""

            try:
                images = soup.findAll("img", {"class": "banner-slider__slide-img"})

                for image in images:
                    arry_photo.append(image.get('src'))

            except Exception as e:
                t = "תמונה: "
                arry_photo.append(t)

            category = "קטגוריה: " + category
            name_of_proudect = "שם המוצר: " + name
            if y != -1:
                make_way = make_way
            else:
                make_way = "אופן הכנה: "
            try:
                if arry_photo[0].find("data") != -1:
                    del arry_photo[0]
            except Exception as e:
                print("")

            data = dish(category,category_type,name_of_proudect,condumens,make_way,tips,preparation_time,cooking_biking_time,num_of_Dishes,arry_photo)
            objects.append(data)

        except Exception as e:
            print(e)

    for i in objects:
        print(i)

if __name__ == '__main__':
    URLScollect()
    date()
