# Tracking Inventory

Write a program that tracks your personal inventory. The program should allow you to enter an item, a serial number, and estimated value. The program should then be able to print out a tabular report in both HTML and CSV formats that looks like this:

| **Name**   | **Serial Number** | **Value** |
| ---------- | ----------------- | --------- |
| Xbox One   | AXB124AXY         | $399.00   |
| Samsung TV | S40AZBDE4         | $599.00   |

## Constraints
- Store the data in a persistent local data file in JSON, XML, or YAML format.
- Require numeric data for the value of each item.