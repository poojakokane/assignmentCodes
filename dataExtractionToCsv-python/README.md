# Dataset Creation

Aggregate JSON files per day and then per month

```bash
E:\assignmentCodes\dataExtractionToCsv-python\venv\Scripts\python.exe E:/assignmentCodes/dataExtractionToCsv-python/main.py inputData outputData
aggregated contents of  inputData\2020\01\01\03\201901010300.json  to  outputData\2020\01\01\aggregated.csv
aggregated contents of  inputData\2020\01\01\03\201901010305.json  to  outputData\2020\01\01\aggregated.csv
aggregated contents of  inputData\2020\01\01\03\201901010310.json  to  outputData\2020\01\01\aggregated.csv
aggregated contents of  inputData\2020\01\01\03\201901010300.json  to  outputData\2020\01\aggregated.csv
aggregated contents of  inputData\2020\01\01\03\201901010305.json  to  outputData\2020\01\aggregated.csv
aggregated contents of  inputData\2020\01\01\03\201901010310.json  to  outputData\2020\01\aggregated.csv

Process finished with exit code 0
```

Note: Currently the functions to aggregate are called with `maxFilesToProcess=3` which makes it stop after processing 3 
JSON files. To run for all files, just remove this variable or set it to 0.