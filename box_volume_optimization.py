import numpy as np
from scipy.optimize import minimize

def calc_volume(x):
    length = x[0]
    width = x[1]
    height = x[2]
    return length * width * height

def cal_surface_area(x):
    length = x[0]
    width = x[1]
    height = x[2]
    return 2*length*width + 2*width*height + 2* height*length

def objective(x):
    return -calc_volume(x)

def constraints(x):
    return 10 - cal_surface_area(x)

cons = ({'type': 'ineq', 'fun': constraints})

lengthGuess = 1
widthGuess = 1
heightGuess = 1

x0 = np.array([lengthGuess, widthGuess, heightGuess])

MINIMIZE_METHODS = ['nelder-mead', 'powell', 'cg', 'bfgs', 'newton-cg',
                    'l-bfgs-b', 'tnc', 'cobyla', 'slsqp', 'trust-constr',
                    'dogleg', 'trust-ncg', 'trust-exact', 'trust-krylov']
opt_method = 'slsqp'
sol = minimize(fun=objective, x0=x0, method=opt_method, constraints=cons, options={'disp': True})

xOpt = sol.x
volumeOpt = -sol.fun

surfaceArea = cal_surface_area(xOpt)

print(f'Length: {xOpt[0]}')
print(f'Width: {xOpt[1]}')
print(f'Height: {xOpt[2]}')
print(f'Volume: {volumeOpt}')
print(f'SurfaceArea: {surfaceArea}')