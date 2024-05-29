```
Commands available:
  help:    		         Shows the help page
  start:               Starts the time tracking
  stop:                Stops the time tracking
  month <maxHours> <month>
                       Display the stats of the current month.
                       You can specify how many hours you want to work in total. (Default: 40)
                       You can specify which month to display. (Default: current month)
  week <maxHours> <weekOfMonth> <month>
                       Display the stats of the current week.
                       You can specify how many hours you want to work in total. (Default 10)
                       This does not take in consideration that a certain week can be in two different months.
                       Each week 'part' will count as its own 'week'.
                       You can specify which week of the current month to display. (Default: current week)
                       You can also specify the month whose week to disply. (Default: current month)
  export <directory>
                       Export the work time of the current month as .csv file.
                       You can specify where to put that file. (Default: Downloads folder)
                       This file is ready to be used inside of excel.
  exportCorrected <directory> <totalHours>
                       Export the corrected version of the current month as .csv file.
                       This does not use local data but rather tries to take in <totalHours> and map them onto the current month regarding leaving out weekends
                       You can specify where to put that file. (Default: Downloads folder).
                       You can also specify how many work hours you should have had in the current month. (Default: 40)
                       This file is ready to be used inside of excel.
----------------------------------------------------------

Output is normally colored...
Example output of 'month':
-----------------------------------------Work Time Tracker---------------------------------------

Total work load for Mai:                        40h
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

Total work load for Mai, Week 5:                10h
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
