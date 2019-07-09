data {
  // number of data in a day
  int N;
  // number of days
  int M;
  real y[M, N];
}

parameters {
  real phi[3];
  real cons;
  real<lower=0> sigma;
}

model {
  sigma ~ uniform(0, 100);
  cons ~ uniform(-100, 100);
  phi[1] ~ uniform(-100, 100);
  phi[2] ~ uniform(-100, 100);
  phi[3] ~ uniform(-100, 100);
  for (j in 1:M) {
    for (i in 6:N) {
      y[j][i] - cons - phi[1] * y[j][i - 1] - phi[2] * y[j][i - 2] - phi[3] * y[j][i - 3] ~ normal(0, 1 / (sigma * sigma));
    }
  }
}
