import requests
import re
from bs4 import BeautifulSoup as bs, BeautifulSoup

URLlist = []


def URLScollect():
    # meat and chiken
    for i in range(1, 12):
        URL = 'https://www.10dakot.co.il/category/%D7%A2%D7%95%D7%A3-%D7%95%D7%91%D7%A9%D7%A8/?page=' + str(i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        URLlist.append(found)
    # brakefast
    for i in range(1, 16):
        URL = 'https://www.10dakot.co.il/category/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%9C%D7%90%D7%A8%D7%95%D7%97%D7%AA-%D7%A2%D7%A8%D7%91/?page=' + str(
            i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        URLlist.append(found)
    # helthy respis
    for i in range(1, 21):
        URL = 'https://www.10dakot.co.il/category/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%91%D7%A8%D7%99%D7%90%D7%99%D7%9D/?page=' + str(
            i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        URLlist.append(found)
    # breds
    for i in range(1, 5):
        URL = 'https://www.10dakot.co.il/category/%d7%9e%d7%90%d7%a4%d7%99%d7%9d/?page=' + str(i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        URLlist.append(found)
    # veigen
    for i in range(1, 32):
        URL = 'https://www.10dakot.co.il/category/%D7%9E%D7%AA%D7%9B%D7%95%D7%A0%D7%99%D7%9D-%D7%98%D7%91%D7%A2%D7%95%D7%A0%D7%99%D7%99%D7%9D/?page=' + str(
            i) + '/'
        req = requests.get(URL)
        found = re.findall("<a class=\"categories__item\" href=\"(.*)\">", req.text)
        URLlist.append(found)


def date():
    for URL in URLlist:
        URL = 'https://www.10dakot.co.il/recipe/%d7%a4%d7%a1%d7%98%d7%94-%d7%a0%d7%a7%d7%a0%d7%99%d7%a7%d7%99%d7%95%d7%aa/'
        req = requests.get(URL)
        r = requests.get(URL)
        soup = BeautifulSoup(r.content, 'html.parser')
        proudects = soup.findAll("div", {"class":"resipes__content"})[0].text
        preparation_time = soup.findAll("div", {"class":"resipes__header-col"})[0].text
        cooking_biking_time = soup.findAll("div", {"class": "resipes__header-col"})[1].text
        num_of_Dishes = soup.findAll("div", {"class": "resipes__header-col"})[2].text
        category_type = soup.findAll("div", {"class": "resipes__header-col"})[3].text
        category = soup.findAll("div", {"class": "banner_resipe__tag"})[0].text
        name = soup.findAll("h1", {"class": "banner_resipe__title"})[0].text






if __name__ == '__main__':
    URLScollect()
    date()
