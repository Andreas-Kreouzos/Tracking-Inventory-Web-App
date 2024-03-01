package util

import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName
import org.testcontainers.utility.MountableFile
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration

class TestContainersSpec extends Specification {

    private static final String LIBERTY_CONFIG_BASE_PATH = 'src/main/liberty/config'
    private static final String CONTAINER_CONFIG_BASE_PATH = 'config'

    @Shared
    GenericContainer openLiberty = new GenericContainer(DockerImageName.parse("icr.io/appcafe/open-liberty:full-java17-openj9-ubi"))
            .withExposedPorts(9080, 9443)
            .withEnv("REDIS_HOST", "redis")
            .withCopyFileToContainer(MountableFile.forHostPath("build/libs/tracking-inventory-0.0.1-SNAPSHOT.war"), "${CONTAINER_CONFIG_BASE_PATH}/apps/tracking-inventory-0.0.1-SNAPSHOT.war")
            .withCopyFileToContainer(MountableFile.forHostPath("${LIBERTY_CONFIG_BASE_PATH}/server.xml"), "${CONTAINER_CONFIG_BASE_PATH}/server.xml")
            .withCopyFileToContainer(MountableFile.forHostPath("${LIBERTY_CONFIG_BASE_PATH}/bootstrap.properties"), "${CONTAINER_CONFIG_BASE_PATH}/bootstrap.properties")
            .withCopyFileToContainer(MountableFile.forHostPath("${LIBERTY_CONFIG_BASE_PATH}/GeneratedSSLInclude.xml"), "${CONTAINER_CONFIG_BASE_PATH}/GeneratedSSLInclude.xml")
            .withCopyFileToContainer(MountableFile.forHostPath("${LIBERTY_CONFIG_BASE_PATH}/users.xml"), "${CONTAINER_CONFIG_BASE_PATH}/users.xml")
            .waitingFor(Wait.forLogMessage(".*CWWKZ0001I: Application .* started in .* seconds.*", 1)).withStartupTimeout(Duration.ofMinutes(2))
            .withLogConsumer(new Slf4jLogConsumer(LoggerFactory.getLogger("openLiberty")))

    def setupSpec() {
        openLiberty.start()
    }
}
