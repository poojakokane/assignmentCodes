import errno
import json
import os
import sys
import zipfile
from datetime import datetime, timezone
from dateutil import tz


def main():
    [ipDirPath, opDirPath] = getInputOutputFileNames()

    # aggregateFilesAtDayLevel(ipDirPath=ipDirPath, opDirPath=opDirPath,
    #                          # maxFilesToProcess=3,
    #                          monthInputSanityCheck=False,
    #                          deleteIntermediateFiles=True
    #                          )
    # aggregateFilesAtMonthLevel(ipDirPath=ipDirPath, opDirPath=opDirPath,
    #                            # maxFilesToProcess=3,
    #                            monthInputSanityCheck=False,
    #                            deleteIntermediateFiles=True
    #                            )

    # redistributeCsvFilesForCstDay(opDirPath, removeExistingCsv=False)
    # redistributeCsvFilesForCstMonth(opDirPath, removeExistingCsv=False)

    partitionOnGivenGidDayLevel(opDirPath, "inputData\\2020\\gidstatic.csv")
    # partitionOnGivenGidMonthLevel(opDirPath, "inputData\\2020\\gidstatic.csv")


def appendLineInFile(line, gidPartitionFileFullPath):

    if not os.path.exists(gidPartitionFileFullPath):
        with open(gidPartitionFileFullPath, "w") as f:
            f.write("gid, tmpc, wawa, ptype, dwpc, smps, drct, vsby, roadtmpc, srad, snwd, pcpn, time_UTC, time_CST\n")
            f.close()
            print("Created partition file ", gidPartitionFileFullPath)

    with open(gidPartitionFileFullPath, "a") as f:
        f.write(line)


def partitionOneFile(cstCsvFile, gidFileFullPath, partitionFileDir):

    with open(gidFileFullPath, "a") as partition:
        with open(cstCsvFile, "r") as csv:
            csv.readline()
            line = csv.readline()
            while line:
                rowData = line.split(sep=',')
                line = csv.readline()

                gid = rowData[0]
                partitionFileName = gid + "_partition.csv"
                gidPartitionFile = os.path.join(partitionFileDir, partitionFileName)
                appendLineInFile(line, gidPartitionFile)


def partitionOnGivenGidMonthLevel(opDirPath, gidFileFullPath, cstCsvFileName="cstAggregated.csv"):
    for year in os.listdir(opDirPath):
        if not os.path.isdir(os.path.join(opDirPath, year)):
            continue

        for month in os.listdir(os.path.join(opDirPath, year)):
            if not os.path.isdir(os.path.join(opDirPath, year, month)):
                continue

            # File that contains aggregated data
            cstCsvFile = os.path.join(opDirPath, year, month, cstCsvFileName)
            partitionOneFile(cstCsvFile, gidFileFullPath, os.path.join(opDirPath, year, month))

            print("Partitioned ", cstCsvFile, " added to ", gidFileFullPath)


def partitionOnGivenGidDayLevel(opDirPath, gidFileFullPath, cstCsvFileName="cstAggregated.csv"):
    for year in os.listdir(opDirPath):
        if not os.path.isdir(os.path.join(opDirPath, year)):
            continue

        for month in os.listdir(os.path.join(opDirPath, year)):
            if not os.path.isdir(os.path.join(opDirPath, year, month)):
                continue

            for day in os.listdir(os.path.join(opDirPath, year, month)):
                if not os.path.isdir(os.path.join(opDirPath, year, month, day)):
                    continue

                # File that contains aggregated data
                cstCsvFile = os.path.join(opDirPath, year, month, day, cstCsvFileName)
                partitionOneFile(cstCsvFile, gidFileFullPath, os.path.join(opDirPath, year, month, day))

                print("Partitioned ", cstCsvFile, " added to ", gidFileFullPath)


def putLastDaySlice(lastCsvFile, utcCsvFile):
    lines = 0
    totalLines = 0
    with open(lastCsvFile, "a") as f:
        with open(utcCsvFile, "r") as csv:
            csv.readline()
            line = csv.readline()
            while line:
                rowData = line.split(sep=',')
                line = csv.readline()

                utcTime = rowData[12]
                cstTime = rowData[13]

                if utcTime.split('T')[0] != cstTime.split('T')[0]:
                    f.write(line)

                lines = lines + 1
                totalLines = totalLines + 1
                if lines >= 100000:
                    print("Processed ", lines, " from ",
                          utcCsvFile, " total lines ", totalLines)
                    lines = 0

        csv.close()
    f.close()


def putCurrentDaySlice(cstCsvFile, utcCsvFile):
    lines = 0
    totalLines = 0
    with open(cstCsvFile, "a") as f:
        with open(utcCsvFile, "r") as csv:
            csv.readline()
            line = csv.readline()
            while line:
                rowData = line.split(sep=',')
                utcTime = rowData[12]
                cstTime = rowData[13]

                if utcTime.split('T')[0] == cstTime.split('T')[0]:
                    f.write(line)

                lines = lines + 1
                totalLines = totalLines + 1
                if lines >= 100000:
                    print("Processed ", lines, " from ",
                          utcCsvFile, " total lines ", totalLines)
                    lines = 0

                line = csv.readline()
        csv.close()
    f.close()


def redistributeCsvFilesForCstDay(opDirPath,
                                  removeExistingCsv=False,
                                  utcCsvFileName="aggregated.csv",
                                  cstCsvFileName="cstAggregated.csv"):
    lastCsvFile = None

    for year in os.listdir(opDirPath):
        if not os.path.isdir(os.path.join(opDirPath, year)):
            continue

        for month in os.listdir(os.path.join(opDirPath, year)):
            if not os.path.isdir(os.path.join(opDirPath, year, month)):
                continue

            for day in os.listdir(os.path.join(opDirPath, year, month)):
                if not os.path.isdir(os.path.join(opDirPath, year, month, day)):
                    continue

                # File that contains aggregated data
                utcCsvFile = os.path.join(opDirPath, year, month, day, utcCsvFileName)
                cstCsvFile = os.path.join(opDirPath, year, month, day, cstCsvFileName)

                if not os.path.exists(cstCsvFile):
                    with open(cstCsvFile, "w") as f:
                        f.write(
                            "gid, tmpc, wawa, ptype, dwpc, smps, drct, vsby, roadtmpc, srad, snwd, pcpn, time_UTC, time_CST\n")
                        f.close()

                if lastCsvFile is not None:
                    putLastDaySlice(lastCsvFile, utcCsvFile)
                    print("Done copying out previous day CST to ", lastCsvFile, " from ",
                          utcCsvFile)

                putCurrentDaySlice(cstCsvFile, utcCsvFile)
                print("Done copying out current day CST to ", cstCsvFile, " from ",
                      utcCsvFile)
                lastCsvFile = cstCsvFile


def redistributeCsvFilesForCstMonth(opDirPath,
                                    removeExistingCsv=False,
                                    utcCsvFileName="aggregated.csv",
                                    cstCsvFileName="cstAggregated.csv"):
    lastCsvFile = None

    for year in os.listdir(opDirPath):
        if not os.path.isdir(os.path.join(opDirPath, year)):
            continue

        for month in os.listdir(os.path.join(opDirPath, year)):
            if not os.path.isdir(os.path.join(opDirPath, year, month)):
                continue

            # File that contains aggregated data
            utcCsvFile = os.path.join(opDirPath, year, month, utcCsvFileName)
            cstCsvFile = os.path.join(opDirPath, year, month, cstCsvFileName)

            # for day in os.listdir(os.path.join(opDirPath, year, month)):
            #     if not os.path.isdir(os.path.join(opDirPath, year, month, day)):
            #         continue

            if not os.path.exists(cstCsvFile):
                with open(cstCsvFile, "w") as f:
                    f.write(
                        "gid, tmpc, wawa, ptype, dwpc, smps, drct, vsby, roadtmpc, srad, snwd, pcpn, time_UTC, "
                        "time_CST\n")
                    f.close()

            if lastCsvFile is not None:
                putLastDaySlice(lastCsvFile, utcCsvFile)
                print("Done copying out previous day CST to ", lastCsvFile, " from ",
                      utcCsvFile)

            putCurrentDaySlice(cstCsvFile, utcCsvFile)
            print("Done copying out current day CST to ", cstCsvFile, " from ",
                  utcCsvFile)

        lastCsvFile = cstCsvFile


def getInputOutputFileNames():
    inputDir = None
    outputDir = None

    if len(sys.argv) < 2:
        print("ERROR!!! Please provide inputDirectoryPath as command line argument")

    inputDir = sys.argv[1]

    if len(sys.argv) > 2:
        outputDir = sys.argv[2]

    return [inputDir, outputDir]


def aggregateFilesAtMonthLevel(ipDirPath,
                               opDirPath=None,
                               maxFilesToProcess=0,
                               opFileName="aggregated.csv",
                               monthInputSanityCheck=False,
                               deleteIntermediateFiles=True):
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

                        jsonFilePath = os.path.join(ipDirPath, year, month, day, hour, jsonFile)
                        aggregateContentsOfJson(jsonFilePath, aggregatedFilePath=opFilePath)

                        if deleteIntermediateFiles:
                            os.remove(jsonFilePath)

                        filesToProcess -= 1;

                        # Break before processing ALL files
                        if filesToProcess == 0:
                            return


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
                putDatainAggregateFile(utcTimeString=utcTimeString, cstTimeString=cstTimeString, dataRow=dataRow,
                                       aggFile=f)

    print("aggregated contents of ", jsonFilePath, " to ", aggregatedFilePath)


def utcTimeStringToCstTimeString(utcTimeString):
    fromZone = tz.gettz('Europe/Dublin')
    toZone = tz.gettz('America/Chicago')
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
                             monthInputSanityCheck=False,
                             deleteIntermediateFiles=False):
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

                        jsonFilePath = os.path.join(ipDirPath, year, month, day, hour, jsonFile)
                        aggregateContentsOfJson(jsonFilePath, aggregatedFilePath=opFilePath)

                        if deleteIntermediateFiles:
                            os.remove(jsonFilePath)

                        filesToProcess -= 1;

                        # Break before processing ALL files
                        if filesToProcess == 0:
                            return


if __name__ == "__main__":
    main()
