import datetime
import ast

class SettingParser:

    def __init__(self, setting_dir):
        self.setting_dir = setting_dir
        self.setting = {}

    def load_setting(self, setting):
        d = {}
        for line in open(self.setting_dir + setting, "r"):
            (key, value) = line.split()
            d[key] = value
        self.setting = d

    def model(self):
        return self.setting["model"]
    
    def date(self):
        d = datetime.datetime.strptime(self.setting["date"], "%Y-%m-%d")
        return datetime.date(d.year, d.month, d.day)
    
    def is_am(self):
        return self.setting["is_am"] == "True"
    
    def code(self):
        return int(self.setting["code"])
    
    def data_size(self):
        return int(self.setting["data_size"])
    
    def trim_interval(self):
        return int(self.setting["trim_interval"])
    
    def window_size(self):
        return int(self.setting["window_size"])
    
    def iter(self):
        return int(self.setting["iter"])
    
    def chains(self):
        return int(self.setting["chains"])
    
    def parser(self):
        return self.setting["parser"]
    
    def forcast(self):
        return self.setting["forcast"]
    
    def output_file_name(self):
        return self.setting["output_file_name"]

    def get(self, key):
        return self.setting[key]

class ParamParser:

    def load_params(self, file_name):
        s = ""
        for line in open(file_name, "r"):
            s = s + line
        return ast.literal_eval(s)
