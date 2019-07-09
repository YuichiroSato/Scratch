import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/../../util")

import ast
import numpy
import math
import datetime
import pandas
import ggplot

from DataLoader import DataLoader
from DataTrimer import DataTrimer

os.environ["DISPLAY"] = ":0"

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

    result = [0 for i in range(0, len(test_data))]
    for i in range(12, len(test_data) - 12):
        y1 = test_data[i]
        y2 = test_data[i - 1]
        y3 = test_data[i - 2]
        
        x0 = simulate_next(y1, y2, y3, cons, phi0, phi1, phi2, sigma)
        x1 = simulate_next(x0, y1, y2, cons, phi0, phi1, phi2, sigma)
        x2 = simulate_next(x1, x0, y1, cons, phi0, phi1, phi2, sigma)
        result[i] = x2
    return result

def load_data(date):
    loader = DataLoader("../../../../StockPrice")
    is_am = True
    code = 8411
    data_size = 1
    trim_interval = 10
    window_size = 20
    return DataTrimer.trim_data_seq(loader.load_data(date, is_am, code, data_size), trim_interval, window_size)

def main():
    params = load_params("./params.txt")
    test_data = load_data(datetime.date(2017, 12, 20))
    sim_data = simulate(test_data[0], params)
    dif_data = [0 for i in range(0, len(test_data[0]))]
    for i in range(0, len(dif_data) - 3):
        dif_data[i] = test_data[0][i + 3] - sim_data[i]

    df = pandas.DataFrame({'t': range(6, len(test_data[0])), 'price': test_data[0][6:]})
    df2 = pandas.DataFrame({'t': range(12, len(sim_data)-12), 'price': sim_data[12:-12]})
    df3 = pandas.DataFrame({'t': range(12, len(sim_data)-12), 'price': dif_data[12:-12]})
    a = ggplot.ggplot(ggplot.aes(x='t', y='price'), data=df) \
            + ggplot.geom_line()
    b = ggplot.ggplot(ggplot.aes(x='t', y='price'), data=df2) \
            + ggplot.geom_line(color='blue')
    c = ggplot.ggplot(ggplot.aes(x='t', y='price'), data=df3) \
            + ggplot.geom_line(color='blue')
    a.save('hoge.png')
    b.save('hoge2.png')
    c.save('hoge3.png')

if __name__ == "__main__":
    main()
