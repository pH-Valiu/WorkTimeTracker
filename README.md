# WorkTimeTracker
## Goal
Having a digital semi-automatic method to tracking your time spent working.

## What we offer
A simple Java command line based tool to keep track of your spent time on different projects.

## Getting started
Please first follow the installation guide.

### Starting
After you completed installation, to start tracking your time, call:
```sh
wtt start <project_name>
```
This starts tracking the time for your project. 
It will create a new project if there did not exist one so far.

### Stopping
To stop the tracking, call:
```sh
wtt stop <project_name>
```

### List projects
To display all projects, call:
```sh
wtt list
```
(or `wtt -l` `wtt --l`)

### Display worked time
You have two options to display the time you have worked.
Either display the time for a specific month or for a specific week:

#### Month
Call:
```sh
wtt month <project_name>
```
to get a visual representation of how much you have worked this month in each week.<br>
To specify a destined work amount, add it in the command as an argument. The default is 40 hours.

You will see the optimal time distribution for each week for the destined work amount as well as the optimal distribution for each week for the remaining hours until you have reached your work goal.

The syntax for this command is:
```sh
wtt month <project_name> <hoursGoal> <month> <year>
```
The last two arguments allow you to further specify which month of which year to display.

#### Week
Call:
```sh
wtt week <project_name>
```
to get a visual representation of how much and when you have worked this week at each day.<br>
To specify a destined work amount, add it in the command as an argument. The default is 10 hours.

You will see the optimal time distribution for each day for the destined work amount as well as the optimal distribution for each day for the remaining hours until you have reached your work goal.

The syntax for this command is:
```sh
wtt week <project_name> <hoursGoal> <weekOfMonth> <month> <year>
```
The last three arguments allow you to further specify which week of with month of which year to display.
The `weekOfMonth` argument is a number between 1 and 6.


### Exporting data
We offer two export variants. Both export the data in a `.csv` format, ready to be inserted in a Excel sheet.

#### True Export
Call:
```sh
wtt export <project_name>
```
to create a true export of your worked time


#### Corrected Export
Call:
```sh
wtt exportCorrected <project_name> <fileToStoreIn> <totalHours>
```
to create a faked export of your destined work time defined by the `totalHours` argument.
This will make sure that you have only worked on weekdays between 8am - 6pm.

## Help
Following is the complete `--help` output:
We further support many aliases for most commands, like:
- `eC` for `exportCorrected`
- `e` for `export`
- `l` for `list`

```
This is a tool to assist you in keeping track of your work time to not create overtime.
When calling 'start' and adding a project name, a new project will automatically be created
The field <project> is mandatary for all commands which list that field.

Commands:
    help                Shows this page
    list                List all current active projects
    start <project>     Start a work session for given project
    stop <project>      Stop the current work session for given project
    
    month <project> <maxHours> <month> <year>
                        Display the stats of the current month for given project.
                        You can specify how many hours you want to work in total. (Default: 40)
                        You can specify which month to display. (Default: current month)
                        You can specify the year whose month to display. (Default: current year)
    week <project> <maxHours> <weekOfMonth> <month> <year>
                        Display the stats of the current week for given project.
                        You can specify how many hours you want to work in total. (Default 10)
                        This does not take in consideration that a certain week can be in two different months.
                        Each week 'part' will count as its own 'week'.
                        You can specify which week of the current month to display. (Default: current week)
                        You can specify the month whose week to disply. (Default: current month)
                        You can specify the year whose month to display. (Default: current year)
    export <project> <directory> <month> <year>
                        Export the work time of the current month for given project as .csv file.
                        You can specify where to put that file. (Default: Downloads folder) 
                        You can specify the month to export. (Default: current month)
                        You can specify the year whose month to export. (Default: current year)
    exportCorrected <project> <directory> <totalHours> <month> <year>
                        Export the corrected version of the work time of the current month for given project as .csv file.
                        You can specify where to put that file. (Default: Downloads folder).
                        You can specify how many work hours you should have had in the current month. (Default: 40)
                        You can specify the month to export. (Default: current month)
                        You can specify the year whose month to export. (Default: current year)
----------------------------------------------------------

Output is normally colored...
Example output of 'month':
-----------------------------------------Work Time Tracker---------------------------------------

Total work load for Mai (2025):                 40h
Already worked off:                             30h 34min
Still have to work off:                         09h 25min
Weekly Distribution:
        1.      |       2.      |       3.      |       4.      |       5       |       6       |
    09h 00min   |    21h 34min  |    ---------  |    ---------  |    ---------  |    ---------  | <- # Accomplished, # Missed
 of 06h 27min   | of 09h 01min  | of 09h 01min  | of 09h 01min  | of 06h 27min  |    ---------  | <- Optimal Distribution

Weekly distribution for remaining work:
        1.      |       2.      |       3.      |       4.      |       5       |       6       |
    ---------   |    02h 32min  |    02h 32min  |    02h 32min  |    01h 48min  |    ---------  |

-------------------------------------------------------------------------------------------------


Example output of 'week':
-----------------------------------------Work Time Tracker-----------------------------------------------------------------------------

Total work load for Mai (2025), Week 5:                10h
Already worked off:                             04h 33min
Still have to work off:                         05h 26min
Daily Distribution:
───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
00:00 |       |     00:00 |       |     00:00 |       |     00:00 |       |     00:00 |       |     00:00 |       |     00:00 |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
02:00 |       |     02:00 |       |     02:00 |       |     02:00 |       |     02:00 |       |     02:00 |       |     02:00 |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
05:00 |       |     05:00 |       |     05:00 |       |     05:00 |       |     05:00 |       |     05:00 |       |     05:00 |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
08:00 |       |     08:00 |       |     08:00 |       |     08:00 |       |     08:00 |       |     08:00 |       |     08:00 |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
      |       |           | ░░░░░ |           | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |
11:00 |       |     11:00 |       |     11:00 | ░░░░░ |     11:00 |       |     11:00 |       |     11:00 |       |     11:00 |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
14:00 |       |     14:00 |       |     14:00 |       |     14:00 |       |     14:00 |       |     14:00 |       |     14:00 |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
      | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |           |       |           |       |
17:00 | ▒▒▒▒▒ |     17:00 |       |     17:00 |       |     17:00 |       |     17:00 |       |     17:00 |       |     17:00 |       |
      | ▒▒▒▒▒ |           |       |           |       |           |       |           |       |           |       |           |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
20:00 |       |     20:00 |       |     20:00 |       |     20:00 |       |     20:00 |       |     20:00 |       |     20:00 |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
      |       |           |       |           |       |           |       |           |       |           |       |           |       |
23:00 |       |     23:00 |       |     23:00 |       |     23:00 |       |     23:00 |       |     23:00 |       |     23:00 |       |
         Mon.                Tue.                Wed.                Thu.                Fri.
───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────


         Mon.       |        Tue.       |        Wed.       |        Thu.       |        Fri.       |        Sat.       |        Sun.       |
      02h 50min     |     00h 21min     |     01h 21min     |     ---------     |     ---------     |     ---------     |     ---------     |
   of 02h 00min     |  of 02h 00min     |  of 02h 00min     |  of 02h 00min     |  of 02h 00min     |     ---------     |     ---------     |

Daily distribution for remaining work:
         Mon.       |        Tue.       |        Wed.       |        Thu.       |        Fri.       |        Sat.       |        Sun.       |
      ---------     |     ---------     |     01h 48min     |     01h 48min     |     01h 48min     |     ---------     |     ---------     |

---------------------------------------------------------------------------------------------------------------------------------------------  
