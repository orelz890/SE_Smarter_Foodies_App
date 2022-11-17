import requests
import re
from bs4 import BeautifulSoup as bs, BeautifulSoup

URLlist = []


def URLScollect():
    # meat and chiken
    # for i in range(1, 12):
    #     URL = 'https://www.10dakot.co.il/category/%D7%A2%D7%95%D7%A3-%D7%95%D7%91%D7%A9%D7%A8/?page=' + str(i) + '/'
    #     req = requests.get(URL)
    #     found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
    #     for X in found:
    #         URLlist.append(X)
    # # brakefast
    # for i in range(1, 16):
    #     URL = 'https://www.10dakot.co.il/category/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%9C%D7%90%D7%A8%D7%95%D7%97%D7%AA-%D7%A2%D7%A8%D7%91/?page=' + str(
    #         i) + '/'
    #     req = requests.get(URL)
    #     found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
    #     URLlist.append(found)
    # # helthy respis
    # for i in range(1, 21):
    #     URL = 'https://www.10dakot.co.il/category/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%91%D7%A8%D7%99%D7%90%D7%99%D7%9D/?page=' + str(
    #         i) + '/'
    #     req = requests.get(URL)
    #     found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
    #     URLlist.append(found)
    # # breds
    for i in range(1, 5):
        URL = 'https://www.10dakot.co.il/category/%d7%9e%d7%90%d7%a4%d7%99%d7%9d/?page=' + str(i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        for X in found:
            URLlist.append(X)
    # # veigen
    # for i in range(1, 32):
    #     URL = 'https://www.10dakot.co.il/category/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%98%D7%91%D7%A2%D7%95%D7%A0%D7%99%D7%99%D7%9D/?page=' + str(
    #         i) + '/'
    #     req = requests.get(URL)
    #     found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
    #     URLlist.append(found)


def date():
    print(URLlist)
    for URL in URLlist:
        print(URL)
        try:
            r = requests.get(URL)
            soup = BeautifulSoup(r.content, 'html.parser')
            proudects = soup.findAll("div", {"class":"resipes__content"})[0].text
            print(type(proudects))

            x = proudects.find("רוצים")
            print(x , "its x")
            condumens = proudects[0:x:1]
            y = proudects.find("אופן ההכנה")
            if (y == -1):
                y = proudects.find("אופן הכנה")
            print(y , "its y")
            z = proudects.find("טיפים")
            print(z , "its z")
            tips =""
            if(z == -1):
                make_way = proudects[y::1]
            else:
                make_way = proudects[y:z:1]
                tips = proudects[z::1]
            preparation_time = soup.findAll("div", {"class":"resipes__header-col"})[0].text
            cooking_biking_time = soup.findAll("div", {"class": "resipes__header-col"})[1].text
            num_of_Dishes = soup.findAll("div", {"class": "resipes__header-col"})[2].text
            category_type = soup.findAll("div", {"class": "resipes__header-col"})[3].text
            category = soup.findAll("div", {"class": "banner_resipe__tag"})[0].text
            name = soup.findAll("h1", {"class": "banner_resipe__title"})[0].text
            print(condumens)
            print(make_way)
            print(tips)
            print(preparation_time)
            print(cooking_biking_time)
            print(num_of_Dishes)
            print(category_type)
            print(category)
            print(name)
        except Exception as e:
            print(e)







if __name__ == '__main__':
    URLScollect()
    date()
