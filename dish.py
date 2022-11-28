class dish:

    def __init__(self, category: str, category_type: str, name_of_proudect: str, condumens: str, make_way: str,
                 tips: str, preparation_time: str, cooking_biking_time: str, num_of_Dishes: str, arry_photo: list):
        self.category = category
        self.category_type = category_type
        self.name_of_proudect = name_of_proudect
        self.condumens = condumens
        self.make_way = make_way
        self.tips = tips
        self.preparation_time = preparation_time
        self.cooking_biking_time = cooking_biking_time
        self.num_of_Dishes = num_of_Dishes
        self.arry_photo = arry_photo

    def set_category(self, category: str):
        self.category = category

    def get_category(self):
        return self.category

    def set_category_type(self, category_type: str):
        self.category_type = category_type

    def get_category_type(self):
        return self.category_type

    def set_name_of_proudect(self, name_of_proudect: str):
        self.name_of_proudect = name_of_proudect

    def get_name_of_proudect(self):
        return self.name_of_proudect

    def set_condumens(self, condumens: str):
        self.condumens = condumens

    def get_condumens(self):
        return self.condumens

    def set_make_way(self, make_way: str):
        self.make_way = make_way

    def get_make_way(self):
        return self.make_way

    def set_tips(self, tips: str):
        self.tips = tips

    def get_tips(self):
        return self.tips

    def set_preparation_time(self, preparation_time: str):
        self.preparation_time = preparation_time

    def get_preparation_time(self):
        return self.preparation_time

    def set_cooking_biking_time(self, cooking_biking_time: str):
        self.cooking_biking_time = cooking_biking_time

    def get_cooking_biking_time(self):
        return self.cooking_biking_time

    def set_num_of_Dishes(self, num_of_Dishes: str):
        self.num_of_Dishes = num_of_Dishes

    def get_num_of_Dishes(self):
        return self.num_of_Dishes

    def set_arry_photo(self, arry_photo: list):
        self.arry_photo = arry_photo

    def get_arry_photo(self):
        return self.arry_photo

    def __str__(self):
        temp = ""
        for i in self.arry_photo:
            temp += i
        x = "" + self.category + "\n" + self.category_type + "\n" + self.name_of_proudect + "\n" + self.condumens + "\n" + \
            self.make_way + "\n" + self.tips + "\n" + self.preparation_time + "\n" + self.cooking_biking_time + "\n" + \
            self.num_of_Dishes + "\n" + temp

        return x
