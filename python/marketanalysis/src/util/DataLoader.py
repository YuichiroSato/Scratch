import os
import datetime

class DataLoader:

    def __init__(self, stock_root):
        self.stock_root = stock_root
    
    def load_data(self, from_date, is_am, code, n):
        file_paths = self.get_data_files(from_date, is_am, code, n)
        loaded = [[0] for i in range(len(file_paths))]
        for i in range(len(file_paths)):
            loaded[i] = self.read_data(file_paths[i])
        return loaded
    
    def read_data(self, filePath):
        data = []
        for line in open(filePath, "r"):
            data = data + [float(line[:-1])]
        return data
    
    def load_raw_data(self, from_date, is_am, code, n):
        file_paths = self.get_data_files(from_date, is_am, code, n)
        loaded = [[''] for i in range(len(file_paths))]
        for i in range(len(file_paths)):
            loaded[i] = self.read_raw_data(file_paths[i])
        return loaded

    def read_raw_data(self, file_path):
        data = []
        for line in open(file_path, 'r'):
            data = data + [line[:-1]]
        return data

    def get_data_files(self, date, isAm, code, n):
        i = 0
        file_paths = []
        while (len(file_paths) < n and i < 100):
            p = self.load_path(date, isAm, code)
            if os.path.isfile(p):
                file_paths = file_paths + [p]
            else:
                i = i + 1
            date = date + datetime.timedelta(days=-1)
        return file_paths
    
    def load_path(self, date, is_am, code):
        return self.stock_root + self.date_to_path(date) + self.to_filename(date, is_am, code)
    
    def date_to_path(self, date):
        yyyy = date.year
        mm = date.month
        dd = date.day
        if mm < 10:
            mm = "0" + str(mm)
        if dd < 10:
            dd = "0" + str(dd)
        return "/" + str(yyyy) + "/" + str(mm) + "/" + str(yyyy) + str(mm) + str(dd) + "/"
    
    def to_filename(self, date, is_am, code):
        yyyy = date.year
        mm = date.month
        dd = date.day
        if mm < 10:
            mm = "0" + str(mm)
        if dd < 10:
            dd = "0" + str(dd)
        ampm = ""
        if is_am:
            ampm = "am"
        else:
            ampm = "pm"
        return str(yyyy) + str(mm) + str(dd) + ampm + "_" + str(code) + ".csv"
