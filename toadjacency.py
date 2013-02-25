#!/usr/bin/python

import sys

adjacency_lists = {}

#print "Loading data..."
for line in open(sys.argv[1], 'r'):
	(source,target,type) = line.strip().split(',')
	if source not in adjacency_lists:
		adjacency_lists[source] = []
	if target not in adjacency_lists:
		adjacency_lists[target] = []

	adjacency_lists[source].append(target)

for source in adjacency_lists.keys():
	sys.stdout.write(source)
	for target in adjacency_lists[source]:
		sys.stdout.write(' ')
		sys.stdout.write(target)
	sys.stdout.write('\n')
