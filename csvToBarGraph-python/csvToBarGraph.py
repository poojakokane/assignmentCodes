import matplotlib.pyplot as plt
import numpy as np
import pandas
import csv


def main():
	#list = readFileToList("inputFile.csv")

	# df = pandas.read_csv('inputData.csv')
	# print(df)
	# df.plot()
	# df.plot(kind='bar', x='Label', y='Value')

	# initializing the titles and rows list 
	fields = [] 
	rows = [] 
	  
	# reading csv file 
	with open("inputData.csv", 'r') as csvfile: 
	    # creating a csv reader object 
	    csvreader = csv.reader(csvfile) 
	      
	    # extracting field names through first row 
	    fields = next(csvreader) 
	  
	    # extracting each data row one by one 
	    for row in csvreader: 
	        rows.append(row) 


	print(fields)
	rows = [[int(v) for v in r] for r in rows]
	print(rows)

	xTicks = fields
	data = rows
	X = np.arange(len(xTicks))
	fig = plt.figure()
	ax = fig.add_axes([0,0,1,1])
	frac = (1.0 / (len(data)+1))
	print(frac) 
	i = 0
	for r in data:
		print(r)
		ax.bar(X + i*frac, r, color = 'b', width = frac)
		i+=1

	ax.set_xticks(X)
	ax.set_xticklabels(xTicks)
	ax.legend()
	plt.show()

if __name__ == "__main__":
	main()