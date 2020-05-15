import errno
import json
import os
import sys
import zipfile
from datetime import datetime
from dateutil import tz


def getInputOutputFileNames():
    inputDir = None
    outputDir = None

    if len(sys.argv) < 2:
        print("ERROR!!! Please provide inputDirectoryPath as command line argument")

    inputDir = sys.argv[1]

    if (len(sys.argv) > 2):
        outputDir = sys.argv[2]

    return [inputDir, outputDir]


def aggregateFilesAtMonthLevel(ipDirPath,
                               opDirPath = None,
                               maxFilesToProcess = 0,
                               opFileName="aggregated.csv",
                               monthInputSanityCheck = False):
    filesToProcess = -1

    if opDirPath == None:
        opDirPath = ipDirPath

    if maxFilesToProcess > 0:
        filesToProcess = maxFilesToProcess

    for year in os.listdir(ipDirPath):
        if not os.path.isdir(os.path.join(ipDirPath, year)):
            continue

        for month in os.listdir(os.path.join(ipDirPath, year)):
            if not os.path.isdir(os.path.join(ipDirPath, year, month)):
                continue

            folderPath = os.path.join(ipDirPath, year, month)
            if monthInputSanityCheck and not isNumberOfDaysFolderCorrect(folderPath):
                print("Skipping ", folderPath, " as it does not contain enough DAY folders")
                continue

            # File that contains aggregated data
            opFilePath = os.path.join(opDirPath, year, month, opFileName)

            for day in os.listdir(os.path.join(ipDirPath, year, month)):
                if not os.path.isdir(os.path.join(ipDirPath, year, month, day)):
                    continue

                for hour in os.listdir(os.path.join(ipDirPath, year, month, day)):
                    if not os.path.isdir(os.path.join(ipDirPath, year, month, day, hour)):
                        continue

                    # Extract all the zip files for this HOUR
                    for zipFile in os.listdir(os.path.join(ipDirPath, year, month, day, hour)):
                        if os.path.isdir(os.path.join(ipDirPath, year, month, day, hour, zipFile)):
                            continue

                        if zipFile.find(".zip") == -1:
                            continue

                        with zipfile.ZipFile(os.path.join(ipDirPath, year, month, day, hour, zipFile)) as z:
                            z.extractall(path=os.path.join(ipDirPath, year, month, day, hour))

                    # loop over all JSON files and aggregate data
                    for jsonFile in os.listdir(os.path.join(ipDirPath, year, month, day, hour)):
                        if os.path.isdir(os.path.join(ipDirPath, year, month, day, hour, jsonFile)):
                            continue

                        if jsonFile.find(".json") == -1:
                            continue

                        aggregateContentsOfJson(jsonFilePath=os.path.join(ipDirPath, year, month, day, hour, jsonFile),
                                                aggregatedFilePath=opFilePath)
                        filesToProcess -= 1;

                        # Break before processing ALL files
                        if filesToProcess == 0:
                            return


def main():
    [ipDirPath, opDirPath] = getInputOutputFileNames()

    aggregateFilesAtDayLevel(ipDirPath=ipDirPath, opDirPath=opDirPath,
                             # maxFilesToProcess=3,
                             monthInputSanityCheck=False
                             )
    aggregateFilesAtMonthLevel(ipDirPath=ipDirPath, opDirPath=opDirPath,
                               # maxFilesToProcess=3,
                               monthInputSanityCheck=False
                               )


def putDatainAggregateFile(utcTimeString, cstTimeString, dataRow, aggFile):

    stringToPutInFile = ""
    for dataField in ["gid", "tmpc"]:
        stringToPutInFile += (str(dataRow[dataField]) + ", ")

    wawaString = " ".join(dataRow["wawa"])
    stringToPutInFile += wawaString + ", "

    for dataField in ["ptype", "dwpc", "smps", "drct", "vsby", "roadtmpc", "srad", "snwd", "pcpn"]:
        stringToPutInFile += (str(dataRow[dataField]) + ", ")

    stringToPutInFile += (utcTimeString + ", ")
    stringToPutInFile += (cstTimeString + "\n")

    aggFile.write(stringToPutInFile)


def aggregateContentsOfJson(jsonFilePath, aggregatedFilePath):

    # Create directory structure if not exists
    if not os.path.exists(os.path.dirname(aggregatedFilePath)):
        try:
            os.makedirs(os.path.dirname(aggregatedFilePath))
        except OSError as exc:  # Guard against race condition
            if exc.errno != errno.EEXIST:
                raise

    if not os.path.exists(aggregatedFilePath):
        with open(aggregatedFilePath, "w") as f:
            f.write("gid, tmpc, wawa, ptype, dwpc, smps, drct, vsby, roadtmpc, srad, snwd, pcpn, time_UTC, time_CST\n")
            f.close()

    with open(aggregatedFilePath, "a") as f:
        with open(jsonFilePath, "r") as jp:
            j = json.load(jp)

            utcTimeString = j["time"]
            cstTimeString = utcTimeStringToCstTimeString(utcTimeString)

            for dataRow in j["data"]:
                putDatainAggregateFile(utcTimeString=utcTimeString, cstTimeString=cstTimeString, dataRow=dataRow, aggFile=f)

    print("aggregated contents of ", jsonFilePath, " to ", aggregatedFilePath)


def utcTimeStringToCstTimeString(utcTimeString):
    fromZone = tz.tzutc()
    toZone = tz.tz.gettz('CST')
    utcTime = datetime.strptime(utcTimeString, '%Y-%m-%dT%H:%M:%SZ')
    utcTime = utcTime.replace(tzinfo=fromZone)
    cstTime = utcTime.astimezone(toZone)
    cstTimeString = cstTime.strftime("%Y-%m-%dT%H:%M:%SZ")
    return cstTimeString


def isNumberOfDaysFolderCorrect(folderPath):
    countOfDaysFolder = 0

    for day in os.listdir(folderPath):
        if not os.path.isdir(os.path.join(folderPath, day)):
            continue

        countOfDaysFolder += 1;

    if countOfDaysFolder < 28:
        return False

    return True



def aggregateFilesAtDayLevel(ipDirPath,
                             maxFilesToProcess=0,
                             opFileName="aggregated.csv",
                             opDirPath=None,
                             monthInputSanityCheck=False):
    filesToProcess = -1

    if opDirPath == None:
        opDirPath = ipDirPath

    if maxFilesToProcess > 0:
        filesToProcess = maxFilesToProcess

    for year in os.listdir(ipDirPath):
        if not os.path.isdir(os.path.join(ipDirPath, year)):
            continue

        for month in os.listdir(os.path.join(ipDirPath, year)):
            if not os.path.isdir(os.path.join(ipDirPath, year, month)):
                continue

            folderPath = os.path.join(ipDirPath, year, month)
            if monthInputSanityCheck and not isNumberOfDaysFolderCorrect(folderPath):
                print("Skipping ", folderPath, " as it does not contain enough DAY folders")
                continue

            for day in os.listdir(os.path.join(ipDirPath, year, month)):
                if not os.path.isdir(os.path.join(ipDirPath, year, month, day)):
                    continue

                # File that contains aggregated data
                opFilePath = os.path.join(opDirPath, year, month, day, opFileName)

                for hour in os.listdir(os.path.join(ipDirPath, year, month, day)):
                    if not os.path.isdir(os.path.join(ipDirPath, year, month, day, hour)):
                        continue

                    # Extract all the zip files for this HOUR
                    for zipFile in os.listdir(os.path.join(ipDirPath, year, month, day, hour)):
                        if os.path.isdir(os.path.join(ipDirPath, year, month, day, hour, zipFile)):
                            continue

                        if zipFile.find(".zip") == -1:
                            continue

                        with zipfile.ZipFile(os.path.join(ipDirPath, year, month, day, hour, zipFile)) as z:
                            z.extractall(path=os.path.join(ipDirPath, year, month, day, hour))

                    # loop over all JSON files and aggregate data
                    for jsonFile in os.listdir(os.path.join(ipDirPath, year, month, day, hour)):
                        if os.path.isdir(os.path.join(ipDirPath, year, month, day, hour, jsonFile)):
                            continue

                        if jsonFile.find(".json") == -1:
                            continue

                        aggregateContentsOfJson(jsonFilePath=os.path.join(ipDirPath, year, month, day, hour, jsonFile),
                                                aggregatedFilePath=opFilePath)
                        filesToProcess -= 1;

                        # Break before processing ALL files
                        if filesToProcess == 0:
                            return


if __name__ == "__main__":
    main()
