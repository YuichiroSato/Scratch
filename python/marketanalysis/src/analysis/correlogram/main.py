import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/../../util")
import math
import datetime

from DataLoader import DataLoader
from Correlogram import Correlogram

def print_correlogram(result):
    for r in range(0, len(result)):
        print(round(result[r], 3), end=" ")
    print("")

def run(correlogram, window_size, trim_interval, n, order):
    result = correlogram.calculate(window_size, trim_interval, n, order)
    print_correlogram(result)
    for i in range(0, order + 1):
        if not Correlogram.is_significant(result[i], n):
            print("till ", str(i))
            break
        if i >= order:
            print("over ", str(order))

def main():
    loader = DataLoader("../../../../StockPrice")
    for tr in range(1, 10):
        print("trim_interval", str(tr))
        print("")
        for d in range(1, 29):
            print("date", str(d))
            from_date = datetime.date(2017, 12, d)
            data = loader.load_data(from_date, True, 8411, 1)
            data[0].reverse()
            correlogram = Correlogram(data[0])
            run(correlogram, 20, tr, 120, 30)

if __name__ == "__main__":
    main()
