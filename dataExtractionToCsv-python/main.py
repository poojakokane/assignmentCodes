import errno
import json
import os
import sys
import zipfile
from datetime import datetime, timezone, timedelta
from dateutil import tz


def main():
    [ipDirPath, opDirPath] = getInputOutputFileNames()

    # Task 1
    aggregateFilesAtDayLevel(ipDirPath=ipDirPath, opDirPath=opDirPath,
                             # maxFilesToProcess=3,
                             monthInputSanityCheck=False,
                             deleteIntermediateFiles=True
                             )
    aggregateFilesAtMonthLevel(ipDirPath=ipDirPath, opDirPath=opDirPath,
                               # maxFilesToProcess=3,
                               monthInputSanityCheck=False,
                               deleteIntermediateFiles=True
                               )

    # Task 2
    redistributeCsvFilesForCstDay(opDirPath, removeExistingCsv=False)
    redistributeCsvFilesForCstMonth(opDirPath, removeExistingCsv=False)

    filterOnGidDayLevel(opDirPath, os.path.join(ipDirPath, '2020', 'gidstatic.csv'));
    filterOnGidMonthLevel(opDirPath, os.path.join(ipDirPath, '2020', 'gidstatic.csv'))

    # Task 3
    joinOFeaturesOnGidAndDateTimePerDay(opDirPath,
                                        os.path.join(ipDirPath, '2020', 'JoinFeatures.csv'),
                                        "gidFilteredFile.csv")
    joinOFeaturesOnGidAndDateTimePerMonth(opDirPath,
                                    os.path.join(ipDirPath, '2020', 'JoinFeatures.csv'),
                                    "gidFilteredFile.csv")


def getListOfJoinFeatureRows(joinFeatureFileFullPath):
    result = []
    mapFeaturesColumns = getAllColumnsMap()

    with open(joinFeatureFileFullPath, 'r') as joinFile:
        joinFile.readline()
        line = joinFile.readline()
        while line:
            oneRow = []
            if ",\"" in line:
                oneRow.extend(line.split("\"")[0].split(',')[0:-1])
                oneRow.append(line.split("\"")[1])
                oneRow.extend(line.split("\"")[2].split(',')[1:])
            else:
                oneRow = line.split(",")

            if len(oneRow) == len(mapFeaturesColumns) - 1:
                result.append(oneRow)

            line = joinFile.readline()

    # print("You may want to fix these rows")
    # for row in result:
    #     if len(row) != 109:
    #         print (len(row), " ", row)

    return result


def mapOfJoinFeatureRowsOfFiveMinIntervals(listOfJoinFeatureRows):
    columnMap = getAllColumnsMap()
    mapToReturn = {}
    for rowData in listOfJoinFeatureRows:
        date = rowData[columnMap["CRASH_DATE"]]
        timeOfCrash = datetime.strptime(rowData[columnMap["TIMESTR"]], '%H:%M')

        timeToSubtract = int(rowData[columnMap["CRASH_TIME"]]) % 5
        timeOfCrashAdjusted = timeOfCrash - timedelta(minutes=timeToSubtract)

        dateOfCrash = datetime.strptime(date, '%Y%m%d')
        crash_time_and_crash_date = dateOfCrash.__str__().split(' ')[0] + 'T' + \
                                    timeOfCrashAdjusted.__str__().split(' ')[1] + 'Z'

        rowData.append(crash_time_and_crash_date)
        # print(dateOfCrash.__str__().split(' ')[0] + 'T' + timeOfCrashAdjusted.__str__().split(' ')[1] + 'Z')

        if crash_time_and_crash_date not in mapToReturn:
            mapToReturn[crash_time_and_crash_date] = [rowData]
        else:
            mapToReturn[crash_time_and_crash_date].append(rowData)

    return mapToReturn


def addRowToJoinFile(joinedFilePath, dataRowString):
    if not os.path.exists(joinedFilePath):
        with open(joinedFilePath, "w") as f:
            f.write("gid, tmpc, wawa, ptype, dwpc, smps, drct, vsby, roadtmpc, srad, snwd, pcpn, time_UTC, time_CST, ")
            f.write(getAllColumnsMap().keys().__str__())
            f.close()


def joinOneFile(cstCsvFile, mapOfJoinFeatureRows, joinedFilePath):
    with open(cstCsvFile, 'r') as csv:
        csv.readline()
        line = csv.readline()
        while line:
            if line[:-1].split(',')[-1] in mapOfJoinFeatureRows:
                dataRowString = line + "," + mapOfJoinFeatureRows[line[-1]].__str__()
                addRowToJoinFile(joinedFilePath, dataRowString)
            line = csv.readline()

        csv.close()


def joinOFeaturesOnGidAndDateTimePerDay(opDirPath, joinFeatureFileFullPath, gidFilteredFileName,
                                        joinedFileName="joinFile.csv"):
    listOfJoinFeatureRows = getListOfJoinFeatureRows(joinFeatureFileFullPath)

    mapOfJoinFeatureRows = mapOfJoinFeatureRowsOfFiveMinIntervals(listOfJoinFeatureRows)

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
                cstCsvFile = os.path.join(opDirPath, year, month, day, gidFilteredFileName)
                joinedFilePath = os.path.join(opDirPath, year, month, day, joinedFileName)

                if os.path.exists(joinedFilePath):
                    os.remove(joinedFilePath)

                joinOneFile(cstCsvFile, mapOfJoinFeatureRows, joinedFilePath)

                print("joined ", cstCsvFile, " added to ", joinedFilePath)


def joinOFeaturesOnGidAndDateTimePerMonth(opDirPath, joinFeatureFileFullPath, gidFilteredFileName,
                                        joinedFileName="joinFile.csv"):
    listOfJoinFeatureRows = getListOfJoinFeatureRows(joinFeatureFileFullPath)

    mapOfJoinFeatureRows = mapOfJoinFeatureRowsOfFiveMinIntervals(listOfJoinFeatureRows)

    for year in os.listdir(opDirPath):
        if not os.path.isdir(os.path.join(opDirPath, year)):
            continue

        for month in os.listdir(os.path.join(opDirPath, year)):
            if not os.path.isdir(os.path.join(opDirPath, year, month)):
                continue


            # File that contains aggregated data
            cstCsvFile = os.path.join(opDirPath, year, month, gidFilteredFileName)
            joinedFilePath = os.path.join(opDirPath, year, month, joinedFileName)

            if os.path.exists(joinedFilePath):
                os.remove(joinedFilePath)

            joinOneFile(cstCsvFile, mapOfJoinFeatureRows, joinedFilePath)

            print("joined ", cstCsvFile, " added to ", joinedFilePath)


def appendLineInFile(line, gidPartitionFileFullPath):
    if not os.path.exists(gidPartitionFileFullPath):
        with open(gidPartitionFileFullPath, "w") as f:
            f.write("gid, tmpc, wawa, ptype, dwpc, smps, drct, vsby, roadtmpc, srad, snwd, pcpn, time_UTC, time_CST\n")
            f.close()
            print("Created filtered file ", gidPartitionFileFullPath)

    with open(gidPartitionFileFullPath, "a") as f:
        f.write(line)


def getAllColumnsMap():
    mapToReturn = {
        "OID": 0,
        "Join_Count": 1,
        "TARGET_FID": 2,
        "CRASH_KEY": 3,
        "CASENUMBER": 4,
        "LECASENUM": 5,
        "XCOORD": 6,
        "YCOORD": 7,
        "REPORTTYPE": 8,
        "DOTDSTRCT": 9,
        "ISPDSTRCT": 10,
        "RPA": 11,
        "MPO": 12,
        "COUNTY": 13,
        "CITYBR": 14,
        "URBANAREA": 15,
        "CVLTWPID": 16,
        "TWNRNGSECT": 17,
        "SCHDST1011": 18,
        "AEA1011": 19,
        "STID1011": 20,
        "DNRDSTRCT": 21,
        "DNRWLDMGMT": 22,
        "DNRWLDDEPR": 23,
        "DNRFLDOFF": 24,
        "CRASH_YEAR": 25,
        "X": 26,
        "Y": 27,
        "Longitude": 28,
        "Latitude": 29,
        "FIRSTHARM": 30,
        "CRCOMANNER": 31,
        "MAJORCAUSE": 32,
        "DRUGALCREL": 33,
        "ECONTCIRC": 34,
        "WEATHER1": 35,
        "WEATHER2": 36,
        "LIGHT": 37,
        "CSURFCOND": 38,
        "ZCOORD": 39,
        "LANEDIR": 40,
        "OVERUNDER": 41,
        "LITDESC": 42,
        "GIMSDATE": 43,
        "LOCTOOLV": 44,
        "CAPTURED": 45,
        "CRASH_DATE": 46,
        "CRASHMONTH": 47,
        "DAYOFMONTH": 48,
        "CRASH_DAY": 49,
        "CRASH_TIME": 50,
        "TIMESTR": 51,
        "TIMEDAY": 52,
        "TIMEBIN": 53,
        "TIMEBIN1": 54,
        "TIMEBIN30": 55,
        "LIGHTING": 56,
        "DAYLIGHT": 57,
        "DARKNESS": 58,
        "LOCFSTHARM": 59,
        "RURALURBAN": 60,
        "CITY": 61,
        "CITYNAME": 62,
        "SYSTEM": 63,
        "ROUTE": 64,
        "SYSTEMSTR": 65,
        "CARDINAL": 66,
        "RAMP": 67,
        "COROADRTE": 68,
        "LITERAL": 69,
        "ROADCLASS": 70,
        "INTCLASS": 71,
        "SYSTEMCONC": 72,
        "RCONTCIRC": 73,
        "ROADTYPE": 74,
        "FRA": 75,
        "PAVED": 76,
        "CSEVERITY": 77,
        "FATALITIES": 78,
        "INJURIES": 79,
        "MAJINJURY": 80,
        "MININJURY": 81,
        "POSSINJURY": 82,
        "UNKINJURY": 83,
        "PROPDMG": 84,
        "VEHICLES": 85,
        "TOCCUPANTS": 86,
        "WZ_RELATED": 87,
        "WZ_LOC": 88,
        "WZ_TYPE": 89,
        "WZ_ACTVTY": 90,
        "WORKERS": 91,
        "gid": 92,
        "LL_row": 93,
        "LL_col": 94,
        "LL_Long": 95,
        "LL_Lat": 96,
        "LR_row": 97,
        "LR_col": 98,
        "LR_Long": 99,
        "LR_Lat": 100,
        "UR_row": 101,
        "UR_col": 102,
        "UR_Long": 103,
        "UR_Lat": 104,
        "UL_row": 105,
        "UL_col": 106,
        "UL_Long": 107,
        "UL_Lat": 108,
        "crash_time_and_crash_date": 109
    }
    return mapToReturn


def filterOneFile(cstCsvFile, setOfGidsTOInclude, filteredFileName):
    count = 0
    includedRows = 0
    with open(cstCsvFile, "r") as csv:
        csv.readline()
        line = csv.readline()
        while line:
            rowData = line.split(sep=',')
            line = csv.readline()

            gid = rowData[0]

            if gid in setOfGidsTOInclude:
                appendLineInFile(line, filteredFileName)
                includedRows += 1

            count += 1
            if count % 100000 == 0:
                print("Processed ", count, " rows from file ", cstCsvFile, " : included ", includedRows, " rows")


def filterOnGidMonthLevel(opDirPath, gidFileFullPath, cstCsvFileName="cstAggregated.csv"):
    setOfGidToKeep = createSetOfGidFromFile(gidFileFullPath);

    for year in os.listdir(opDirPath):
        if not os.path.isdir(os.path.join(opDirPath, year)):
            continue

        for month in os.listdir(os.path.join(opDirPath, year)):
            if not os.path.isdir(os.path.join(opDirPath, year, month)):
                continue

            # File that contains aggregated data
            cstCsvFile = os.path.join(opDirPath, year, month, cstCsvFileName)
            gidFilteredFilePath = os.path.join(opDirPath, year, month, "gidFilteredFile.csv")

            if os.path.exists(gidFilteredFilePath):
                os.remove(gidFilteredFilePath)

            filterOneFile(cstCsvFile, setOfGidToKeep, gidFilteredFilePath)

            print("Filtered ", cstCsvFile, " added to ", gidFilteredFilePath)


def createSetOfGidFromFile(gidFileFullPath):
    result = set()
    print("Creating set of GIDs used to filter in rows...")
    count = 0
    with open(gidFileFullPath, "r") as f:
        f.readline()
        line = f.readline()
        while line:
            line = f.readline()
            result.add(line.split('\n')[0])
            count += 1
            if count % 10000 == 0:
                print("Processed ", count, " rows...")

    print("Gid set created with ", count, " entries")
    return result


def filterOnGidDayLevel(opDirPath, gidFileFullPath, cstCsvFileName="cstAggregated.csv"):
    setOfGidToKeep = createSetOfGidFromFile(gidFileFullPath);

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
                gidFilteredFilePath = os.path.join(opDirPath, year, month, day, "gidFilteredFile.csv")

                if os.path.exists(gidFilteredFilePath):
                    os.remove(gidFilteredFilePath)

                filterOneFile(cstCsvFile, setOfGidToKeep, gidFilteredFilePath)

                print("Filtered ", cstCsvFile, " added to ", gidFilteredFilePath)


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
