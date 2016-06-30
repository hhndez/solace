environment {
	name = 'local'
	jrePath = '/usr/lib/jvm/java-1.8.0-openjdk-amd64'
	llmhome = '/Datos/ibm/llm'

	app {
		jmxPort = '11500'
		memParams = '-Xms256m -Xmx512m'
		gcParams = '-XX:+UseParallelGC'
		debugPort = '11501'
		processName = ''
		home = '/Datos/Trabajo/MassiveTradeOperations'
		logPath = '/Datos/Trabajo/MassiveTradeOperations/log'
		encoding = 'ISO-8859-1'
		maxLogFiles = '20'
	}
	
	logging {
	
	}
}
