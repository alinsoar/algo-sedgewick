.PHONY:

# true or anything else	
DEBUG=false

#WD=$(shell pwd)/
WD=./
SRC=$(WD)src/
PD=$(SRC)Percolation/
QD=$(SRC)Queue/
PRD=$(SRC)Collinear/
P8D=$(SRC)8puzzle/
KD=$(SRC)KdTree/
WND=$(SRC)WordNet/
SCD=$(SRC)SeamCarving/
BED=$(SRC)Baseball/
BOD=$(SRC)Boggle/
BWD=$(SRC)BurrowsWheeler/

JPROF=-Xrunhprof:depth=0,cpu=times,heap=sites,verbose=n
#-agentlib:hprof=file=hprof.txt,lineno=y,depth=0
JLIBOPT=-cp 'bin:libs/*' -ea 
ifeq ($(DEBUG),true)
	JOPT=$(JPROF) $(JLIBOPT) 
else
	JOPT=$(JLIBOPT) 
endif

all:
	@echo "compile"
	@mkdir -p bin/
#	@javac -d bin/ -extdirs libs/ $(SRC)Percolation/*.java
#	@javac -d bin/ -extdirs libs/ $(SRC)Queue/*.java
#	@javac -d bin/ -extdirs libs/ $(SRC)Collinear/*.java
#	@javac -d bin/ -extdirs libs/ $(SRC)8puzzle/*.java
#	@javac -d bin/ -extdirs libs/ $(SRC)KdTree/*.java
#	@javac -d bin -extdirs libs/ $(SRC)WordNet/*.java
#	@javac -d bin -extdirs libs/ $(SRC)SeamCarving/*.java
#	@javac -d bin -extdirs libs/ $(SRC)Baseball/*.java
#	@javac -d bin -extdirs libs/ $(SRC)Boggle/*.java
	javac -d bin -extdirs libs/ $(SRC)BurrowsWheeler/*.java

test:
	@echo "test one of the following"
	@echo perc queue pr 8puzzle kdtree wordnet seamcarving baseball boggle burrows

perc: all
	@echo percolation
	@java -cp 'bin:libs/*' -ea PercolationStats 200 100
#	@java -cp 'bin:libs/*' -ea InteractivePercolationVisualizer 5
#	@java -cp 'bin:libs/*' -ea PercolationVisualizer 20
	java -cp 'bin:libs/*' -ea PercolationVisualizer $(PD)/input/tinyUF.txt
	java -cp 'bin:libs/*' -ea PercolationVisualizer $(PD)/input/mediumUF.txt
	java -cp 'bin:libs/*' -ea PercolationVisualizer $(PD)/input/largeUF.txt

queue: all
	@echo queue
#	echo A B C D E F G H I | java -cp 'bin:libs/*' -ea Subset 3
	cat $(QD)/input/input50.txt | java -cp 'bin:libs/*' -ea Subset 30
	echo AA BB BB BB BB BB CC CC | java -cp 'bin:libs/*' -ea Subset 8

pr: all
	@echo pattern recognition
	java -cp 'bin:libs/*' -ea Brute $(PRD)/input/input8.txt
	java -cp 'bin:libs/*' -ea Fast $(PRD)/input/input6.txt

8puzzle: all
	@echo 8 puzzle

kdtree: all
	@echo kd-tree test
#	java -cp 'bin:libs/*' -ea KdTreeVisualizer
#	java -cp 'bin:libs/*' -ea RangeSearchVisualizer $(KD)input/circle10.txt
	java -cp 'bin:libs/*' -ea NearestNeighborVisualizer $(KD)input/input1M.txt

wordnet: all
#	java -cp 'bin:libs/*' -ea WordNet $(WND)input/synsets.txt\
					$(WND)input/hypernyms11AmbiguousAncestor.txt
#	java -cp 'bin:libs/*' -ea WordNet $(WND)input/synsets.txt\
					$(WND)input/hypernyms.txt
#	java -cp 'bin:libs/*' -ea WordNet $(WND)input/synsets15.txt\
					$(WND)input/hypernymsTree15.txt
#	java -cp 'bin:libs/*' -ea WordNet $(WND)input/synsets3.txt\
					$(WND)input/hypernymsInvalidCycle.txt
#	java -cp 'bin:libs/*' -ea WordNet $(WND)input/synsets3.txt\
					$(WND)input/hypernymsInvalidTwoRoots.txt
#	java -cp 'bin:libs/*' -ea WordNet $(WND)input/synsets8.txt\
					$(WND)input/hypernyms8ManyAncestors.txt
#	java -cp 'bin:libs/*' -ea WordNet $(WND)input/synsets8.txt\
					$(WND)input/hypernyms8WrongBFS.txt
#	java -cp 'bin:libs/*' -ea SAP $(WND)input/digraph1.txt 3 11
#	java -cp 'bin:libs/*' -ea SAP $(WND)input/digraph2.txt 1 5
#	echo " 3 11 9 12 7 2 1 6" | java -cp 'bin:libs/*' -ea SAP $(WND)input/digraph1.txt
#	java -cp 'bin:libs/*' -ea Outcast $(WND)input/synsets.txt\
					$(WND)input/hypernyms.txt\
					$(WND)input/outcast5.txt\
					$(WND)input/outcast8.txt\
					$(WND)input/outcast11.txt
	java -cp 'bin:libs/*' -ea WordNet $(WND)input/synsets.txt\
					$(WND)input/hypernyms.txt

seamcarving: all
	@echo seam carving
#	java -cp 'bin:libs/*' -ea SeamCarver
#	java -cp 'bin:libs/*' -ea SeamCarver $(SCD)input/6x5.png
#	java -cp 'bin:libs/*' -ea ShowEnergy $(SCD)input/6x5.png
#	java -cp 'bin:libs/*' -ea ShowEnergy $(SCD)input/HJoceanSmall.png
#	java -cp 'bin:libs/*' -ea ShowSeams $(SCD)input/HJoceanSmall.png
#	java -cp 'bin:libs/*' -ea PrintSeams $(SCD)input/5x6.png
#	java -cp 'bin:libs/*' -ea PrintSeams $(SCD)input/6x5.png
#	java -cp 'bin:libs/*' -ea PrintSeams $(SCD)input/3x7.png
#	java -cp 'bin:libs/*' -ea PrintSeams $(SCD)input/4x6.png
#	java -cp 'bin:libs/*' -ea PrintSeams $(SCD)input/10x12.png
#	java -cp 'bin:libs/*' -ea ShowSeams $(SCD)input/HJoceanSmall.png
#	java -cp 'bin:libs/*' -ea PrintEnergy $(SCD)input/10x12.png
#	java -cp 'bin:libs/*' -ea ResizeDemo $(SCD)input/HJoceanSmall.png 200 200
	java -cp 'bin:libs/*' -ea ResizeDemo $(SCD)input/pylint.png 200 0

baseball: all
	@echo baseball elimination
	java -cp 'bin:libs/*' -ea BaseballElimination $(BED)input/teams4.txt
#	java -cp 'bin:libs/*' -ea BaseballElimination $(BED)input/teams4a.txt
	java -cp 'bin:libs/*' -ea BaseballElimination $(BED)input/teams5.txt
#	java -cp 'bin:libs/*' -ea BaseballElimination $(BED)input/teams5b.txt
#	java -cp 'bin:libs/*' -ea BaseballElimination $(BED)input/teams12.txt
	java -cp 'bin:libs/*' -ea BaseballElimination $(BED)input/teams24.txt
#	java -cp 'bin:libs/*' -ea BaseballElimination $(BED)input/teams1.txt

boggle: all
	@echo boggle game
#	@java -cp 'bin:libs/*' -ea TernaryTrie $(BOD)input/dictionary-test.txt
#	@java -cp 'bin:libs/*' -ea BoggleSolver $(BOD)input/dictionary-algs4.txt\
						$(BOD)input/board4x4.txt
#	@java -cp 'bin:libs/*' -ea BoggleSolver $(BOD)input/dictionary-yawl.txt\
						$(BOD)input/board-points5.txt
#	@java -cp 'bin:libs/*' -ea BoggleSolver $(BOD)input/dictionary-test.txt\
						$(BOD)input/board-q.txt
#	@java -cp 'bin:libs/*' -ea BoggleSolver $(BOD)input/dictionary-test.txt\
						$(BOD)input/board-pneumonoultramicroscopicsilicovolcanoconiosis.txt
#	@java -cp 'bin:libs/*' -ea BoggleSolver $(BOD)input/dictionary-test.txt\
						$(BOD)input/board-antidisestablishmentarianisms.txt
#	@java -cp 'bin:libs/*' -ea BoggleSolver $(BOD)input/dictionary-yawl.txt\
						$(BOD)input/board-aqua.txt
	java $(JOPT) BoggleSolver $(BOD)input/dictionary-algs4.txt\
				$(BOD)input/board-q.txt
#	@java -cp 'bin:libs/*' -ea BoggleSolver $(BOD)input/dictionary-nursery.txt\
						$(BOD)input/board4x4.txt
#	@java -cp 'bin:libs/*' -ea BoggleSolver $(BOD)input/dictionary-yawl.txt\
						$(BOD)input/board-qwerty.txt
#	@java -cp 'bin:libs/*' -ea BoggleSolver $(BOD)input/dictionary-zingarelli2005.txt\
						$(BOD)input/board-qwerty.txt
#	java -cp 'bin:libs/*' -ea BoggleGame

burrows: all
#	java $(JOPT) HexDump 16 < $(BWD)input/abra.txt
#	java $(JOPT) Huffman - < $(BWD)input/abra.txt | java $(JOPT) HexDump 16
#	java $(JOPT) MoveToFront + < $(BWD)input/abra.txt | java $(JOPT) HexDump 16
#	java $(JOPT) MoveToFront + < $(BWD)input/abra.txt | java $(JOPT) MoveToFront -
#	java $(JOPT) MoveToFront + < $(BWD)input/abra.txt
#	java $(JOPT) MoveToFront + < $(BWD)input/world192.txt
#	java $(JOPT) MoveToFront + < $(BWD)input/princeton.txt | java $(JOPT) MoveToFront -
#	java $(JOPT) MoveToFront + < $(BWD)input/pi-10million.txt | java $(JOPT) MoveToFront -
#	java $(JOPT) MoveToFront + < $(BWD)input/redundant-2copies.txt
#	java $(JOPT) BurrowsWheeler - < $(BWD)input/abra.txt
#	java $(JOPT) BurrowsWheeler - < $(BWD)input/abra.txt | java $(JOPT) HexDump 16
#	java $(JOPT) BurrowsWheeler - < $(BWD)input/abra.txt | java $(JOPT) BurrowsWheeler +
#	java $(JOPT) CircularSuffixArray < $(BWD)input/princeton.txt
#	java $(JOPT) CircularSuffixArray < $(BWD)input/muchado.txt
#	java $(JOPT) CircularSuffixArray < $(BWD)input/abra.txt
#	java $(JOPT) CircularSuffixArray < $(BWD)input/pi-10million.txt
#	java $(JOPT) CircularSuffixArray < $(BWD)input/redundant-2copies.txt
	java $(JOPT) CircularSuffixArray < $(BWD)input/bible.txt
#	java $(JOPT) CircularSuffixArray < $(BWD)input/babble.txt
#	java $(JOPT) BurrowsWheeler - < $(BWD)input/pi-1million.txt |\
	java $(JOPT) MoveToFront + |\
	>out java $(JOPT) Huffman -
#	java $(JOPT) MoveToFront - < $(BWD)input/abra.txt | java $(JOPT) HexDump 16

zip:
	@mkdir -p zip
	@zip -j zip/percolation $(PD)Percolation.java $(PD)PercolationStats.java
	@zip -j zip/queues $(QD)Deque.java $(QD)RandomizedQueue.java $(QD)Subset.java
	@zip -j zip/collinear $(PRD)Point.java $(PRD)Brute.java $(PRD)Fast.java
	@zip -j zip/8puzzle $(P8D)Board.java $(P8D)Solver.java
	@zip -j zip/kdtree $(KD)KdTree.java $(KD)PointSET.java
	@zip -j zip/wordnet $(WND)SAP.java $(WND)WordNet.java $(WND)Outcast.java
	@zip -j zip/seamcarving $(SCD)SeamCarver.java
	@zip -j zip/baseball $(BED)BaseballElimination.java
	@zip -j zip/boggle $(BOD)BoggleSolver.java $(BOD)TernaryTrie.java
	@zip -j zip/burrowswheeler	$(BWD)BurrowsWheeler.java\
					$(BWD)MoveToFront.java\
					$(BWD)CircularSuffixArray.java\
					$(BWD)RBT.java

checkstyle:
	checkstyle -c sun_checks.xml -r src/Baseball/*.java

clean:
	rm -rf bin/ libs/*.java libs/*.class libs/META-INF zip/
	mkdir -p backup-all
	find . -name "backup-all" -prune -o -name '*~' -exec mv -S "~"\
               --backup=numbered '{}' backup-all/ \;

export:
	-mkdir -p bin/
	git archive --format tgz -o ../$(TD).tar.gz HEAD . --prefix=$(TD)/

