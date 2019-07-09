import sys, os
sys.path.append(os.path.dirname(os.path.abspath(__file__)) + "/../../util")
import math
import pystan
import numpy
import datetime

from DataLoader import DataLoader
from DataTrimer import DataTrimer

stan_model_dir = "./"

def create_stan_model(model):
    return pystan.StanModel(file=stan_model_dir + model)

def sampling(stan_model, data, n, m):
    date_size = len(data)
    data_size = len(data[0])
    dic = { 'y': data, 'M' : date_size, 'N': data_size }
    return stan_model.sampling(data=dic, iter=n, chains=m)

def parse_vec(vec, i):
    v = []
    for a in vec:
        v = v + [a[i]]
    return v

def parse_params(params):
    cons_vec = params["cons"]
    phi_vec = params["phi"]
    sigma_vec = params["sigma"]
    return {
        "cons" : numpy.mean(cons_vec),
        "phi0" : numpy.mean(parse_vec(phi_vec, 0)),
        "phi1" : numpy.mean(parse_vec(phi_vec, 1)),
        "phi2" : numpy.mean(parse_vec(phi_vec, 2)),
        "sigma" : numpy.mean(sigma_vec),
        }

def main():
    loader = DataLoader("../../../../StockPrice")
    date = datetime.date(2017, 12, 29)
    is_am = True
    code = 8411
    data_size = 5
    trim_interval = 10
    window_size = 20
    training_data = DataTrimer.trim_data_seq(loader.load_data(date, is_am, code, data_size), trim_interval, window_size)

    stan_model = create_stan_model("model.stan")

    iter = 1000
    chains = 5
    samples = sampling(stan_model, training_data, iter, chains)
    params = samples.extract()
    parsed_param = parse_params(params)

    f = open("params.txt", "w")
    f.write(str(parsed_param) + "\n")
    f.close()

if __name__ == "__main__":
    main()
