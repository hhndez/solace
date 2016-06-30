environment {
	name = 'local'
	jrePath = '/usr/lib/jvm/java-1.8.0-openjdk-amd64'
	llmhome = '/Datos/ibm/llm'

	app {
		jmxPort = '11500'
		memParams = '-Xms256m -Xmx512m'
		gcParams = '-XX:+UseParallelGC'
		debugPort = '11501'
		processName = 'MassiveTrade'
		home = '/Datos/Trabajo/MassiveTradeOperations'
		logPath = '/Datos/Trabajo/MassiveTradeOperations/log'
		encoding = 'ISO-8859-1'
		maxLogFiles = '20'
        topics {
            inputTopics = ''
            massiveTradeToCC ='massive.response.cc'
            massiveTradeToMonet='massive.operation.monet'
			monetToMassiveTrade='massive.trading.cm.seqn.group'
            indexTopics = 'h2h.mci.topic.in.group0,h2h.mci.topic.in.group1,h2h.mci.topic.in.group2,h2h.mci.topic.in.group3,h2h.mci.topic.in.group4,h2h.mci.topic.in.group5,h2h.mci.topic.in.group6,h2h.mci.topic.in.group7,h2h.mci.topic.in.group8,h2h.mci.topic.in.group9,h2h.mci.topic.in'
        }
        marketMandatory = 'RV'
	}
	
	cfp {
		instanceName = 'MassiveTrade'
		cfpType = '1'
		environment = 'local'
		tier {
			memberName = 'MassiveTrade'
			controlAddress = '232.1.1.1'
			controlPort = '4051'
			controlTopicName = 'massiveTradeControlTopic'
			isRealSynchrony = 'true'
			isSingleton = 'true'
			isTotalOrder = 'true'
			logLevel = '5'
			snapshotCycleMilli = '20000'
			syncPeriodMicro = '-1'
			maxSyncAttempts = '5'
			syncIntervalMilli = '60000'
			recoveryTimeoutMilli = '0'
			orderInfoMaxMemory = '10000000'
			members {
				lider {
					name = 'default'
					tierAddress = '232.1.1.2'
					tierPort = '4052'
					priority = '0'
				}
			}
		}
		receiver {
			ackFeedback = '1'
			nackFeedback = '1'
			ancillaryParams = 'SnapshotCycleMilli=20000'
			dataPort = '13136'
			ipVersion = '1'
			logLevel = '9'
			maxMemoryAllowedKBytes = '1000000'
			multicastInterface = 'eth0'
			multicastLoop = 'true'
			protocol = '1'
			socketBufferSizeKBytes = '8192'
			multicastGroup = '239.100.200.99'
			RxTopic {
				reliability = '10'
				maxPendingPackets = '0'
				maxQueueingTimeMilli = '0'
			}
		}
	}

	logging {
	
	}
}
