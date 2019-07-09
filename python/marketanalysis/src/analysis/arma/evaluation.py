import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/../../util")

import ast
import numpy
import math
import datetime

from DataLoader import DataLoader
from DataTrimer import DataTrimer

def load_params(file_name):
    s = ""
    for line in open("./" + file_name, "r"):
        s = s + line
    return ast.literal_eval(s)

def simulate_next(y1, y2, y3, cons, phi0, phi1, phi2, sigma):
    rads = numpy.random.normal(0, 1 / (sigma * sigma), 1000)
    result = [0 for i in range(0, 1000)]
    for i in range(0, 1000):
        result[i] = cons + phi0 * y1 + phi1 * y2 + phi2 * y3 + rads[i]
    return numpy.mean(result)

def simulate(test_data, params):
    cons = params["cons"]
    phi0 = params["phi0"]
    phi1 = params["phi1"]
    phi2 = params["phi2"]
    sigma = params["sigma"]

    for i in range(12, len(test_data) - 12):
        y1 = test_data[i]
        y2 = test_data[i - 1]
        y3 = test_data[i - 2]
        
        x0 = simulate_next(y1, y2, y3, cons, phi0, phi1, phi2, sigma)
        x1 = simulate_next(x0, y1, y2, cons, phi0, phi1, phi2, sigma)
        x2 = simulate_next(x1, x0, y1, cons, phi0, phi1, phi2, sigma)
        print(str(test_data[i + 2]) + " " + str(x1) + "   : " + str(x1 - test_data[i + 2]), end= " ")
        if math.fabs(x1 - test_data[i + 2]) < 0.1:
            print(" OOO ")
        else:
            print(" xxx ")

def load_data():
    loader = DataLoader("../../../../StockPrice")
    date = datetime.date(2017, 12, 20)
    is_am = True
    code = 8411
    data_size = 1
    trim_interval = 10
    window_size = 20
    return DataTrimer.trim_data_seq(loader.load_data(date, is_am, code, data_size), trim_interval, window_size)

def main():
    params = load_params("./params.txt")
    test_data = load_data()
    simulate(test_data[0], params)

if __name__ == "__main__":
    main()
