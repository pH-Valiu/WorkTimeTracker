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
