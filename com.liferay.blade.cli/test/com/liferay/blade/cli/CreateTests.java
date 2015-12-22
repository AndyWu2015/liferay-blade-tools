package com.liferay.blade.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import aQute.lib.io.IO;

public class CreateTests {

	@BeforeClass
	public static void copyTemplates() throws Exception {
		IO.copy(new File("templates.zip"), new File("bin_test/templates.zip"));
	}

	@Test
	public void createProjectAllDefaults() throws Exception {
		String [] args = {
				"create",
				"-d",
				"generated/test",
				"foo"
		};

		blade.main(args);

		assertTrue(IO.getFile("generated/test/foo").exists());

		assertTrue(IO.getFile("generated/test/foo/bnd.bnd").exists());

		File portletFile =
			IO.getFile("generated/test/foo/src/main/java/foo/FooPortlet.java");

		assertTrue(portletFile.exists());

		String portletFileContent = new String(IO.read(portletFile));

		contains(
			portletFileContent,
			".*^public class FooPortlet extends MVCPortlet.*$");

		File gradleBuildFile = IO.getFile("generated/test/foo/build.gradle");

		assertTrue(gradleBuildFile.exists());

		String gradleBuildFileContent = new String(IO.read(gradleBuildFile));

		contains(gradleBuildFileContent,
			".*^apply plugin: \"com.liferay.plugin\".*");

		File viewJSPFile = IO.getFile(
			"generated/test/foo/src/main/resources/META-INF/resources/view.jsp");

		assertTrue(viewJSPFile.exists());

		File initJSPFile = IO.getFile(
			"generated/test/foo/src/main/resources/META-INF/resources/init.jsp");

		assertTrue(initJSPFile.exists());
	}

	@Test
	public void createWorkspaceProjectAllDefaults() throws Exception {
		String [] args = {
				"create",
				"-d",
				"generated/test/workspace/modules/apps",
				"foo"
		};

		File workspace = new File("generated/test/workspace");

		makeWorkspace(workspace);

		blade.main(args);

		assertTrue(IO.getFile("generated/test/workspace/modules/apps/foo").exists());

		assertTrue(IO.getFile("generated/test/workspace/modules/apps/foo/bnd.bnd").exists());

		File portletFile =
			IO.getFile("generated/test/workspace/modules/apps/foo/src/main/java/foo/FooPortlet.java");

		assertTrue(portletFile.exists());

		String portletFileContent = new String(IO.read(portletFile));

		contains(
			portletFileContent,
			".*^public class FooPortlet extends MVCPortlet.*$");

		File gradleBuildFile = IO.getFile("generated/test/workspace/modules/apps/foo/build.gradle");

		assertTrue(gradleBuildFile.exists());

		String gradleBuildFileContent = new String(IO.read(gradleBuildFile));

		lacks(gradleBuildFileContent,
			".*^apply plugin: \"com.liferay.plugin\".*");
	}

	private void makeWorkspace(File workspace) throws IOException {
		workspace.mkdirs();

		new File(workspace, "modules").mkdir();
		new File(workspace, "themes").mkdir();
		new File(workspace, "build.gradle").createNewFile();
	}

	@Test
	public void createGradleMVCPortletProject() throws Exception {
		String [] args = {
				"create",
				"-d",
				"generated/test",
				"-t",
				"mvcportlet",
				"foo"
		};

		blade.main(args);

		assertTrue(IO.getFile("generated/test/foo").exists());

		assertTrue(IO.getFile("generated/test/foo/bnd.bnd").exists());

		File portletFile =
			IO.getFile("generated/test/foo/src/main/java/foo/FooPortlet.java");

		assertTrue(portletFile.exists());

		String portletFileContent = new String(IO.read(portletFile));

		contains(
			portletFileContent,
			".*^public class FooPortlet extends MVCPortlet.*$");

		File gradleBuildFile = IO.getFile("generated/test/foo/build.gradle");

		assertTrue(gradleBuildFile.exists());

		String gradleBuildFileContent = new String(IO.read(gradleBuildFile));

		contains(gradleBuildFileContent,
			".*^apply plugin: \"com.liferay.plugin\".*");

		File viewJSPFile = IO.getFile(
			"generated/test/foo/src/main/resources/META-INF/resources/view.jsp");

		assertTrue(viewJSPFile.exists());

		File initJSPFile = IO.getFile(
			"generated/test/foo/src/main/resources/META-INF/resources/init.jsp");

		assertTrue(initJSPFile.exists());
	}

	public void createGradleMVCPortletProjectWithPackage() throws Exception {
		String [] args = {
				"create",
				"-d",
				"generated/test",
				"-t",
				"mvcportlet",
				"-p",
				"com.liferay.test",
				"foo"
		};

		blade.main(args);

		assertTrue(IO.getFile("generated/test/foo").exists());

		assertTrue(IO.getFile("generated/test/foo/bnd.bnd").exists());

		File portletFile =
			IO.getFile("generated/test/foo/src/main/java/com/liferay/test/FooPortlet.java");

		assertTrue(portletFile.exists());

		String portletFileContent = new String(IO.read(portletFile));

		contains(
			portletFileContent,
			".*^public class FooPortlet extends MVCPortlet.*$");

		File gradleBuildFile = IO.getFile("generated/test/foo/build.gradle");

		assertTrue(gradleBuildFile.exists());

		String gradleBuildFileContent = new String(IO.read(gradleBuildFile));

		contains(gradleBuildFileContent,
			".*^apply plugin: \"com.liferay.plugin\".*");

		File viewJSPFile = IO.getFile(
			"generated/test/foo/src/main/resources/META-INF/resources/view.jsp");

		assertTrue(viewJSPFile.exists());

		File initJSPFile = IO.getFile(
			"generated/test/foo/src/main/resources/META-INF/resources/init.jsp");

		assertTrue(initJSPFile.exists());
	}

	@Test
	public void createGradlePortletProject() throws Exception {
		String [] args = {
				"create",
				"-d",
				"generated/test",
				"-t",
				"portlet",
				"-c",
				"Foo",
				"gradle.test"
		};

		blade.main(args);

		assertTrue(
			IO.getFile("generated/test/gradle.test/build.gradle").exists());

		File portletFile = IO.getFile(
			"generated/test/gradle.test/src/main/java/gradle/test/FooPortlet.java");

		assertTrue(portletFile.exists());

		String portletFileContent = new String(IO.read(portletFile));

		contains(portletFileContent, "^package gradle.test;.*");

		contains(portletFileContent,
			".*javax.portlet.display-name=Gradle.test.*");

		contains(portletFileContent, ".*^public class FooPortlet .*");

		contains(portletFileContent,
			".*printWriter.print\\(\\\"Gradle.test Portlet.*");

		File bndFile = IO.getFile("generated/test/gradle.test/bnd.bnd");

		assertTrue(bndFile.exists());

		String bndFileContent = new String(IO.read(bndFile));

		contains(bndFileContent, ".*^Private-Package: \\\\.*^\tgradle.test$.*");
	}

	@Test
	public void createWorkspaceGradlePortletProject() throws Exception {
		String [] args = {
				"create",
				"-d",
				"generated/test/workspace/modules/apps",
				"-t",
				"portlet",
				"-c",
				"Foo",
				"gradle.test"
		};

		File workspace = new File("generated/test/workspace");

		makeWorkspace(workspace);

		blade.main(args);

		assertTrue(
			IO.getFile("generated/test/workspace/modules/apps/gradle.test/build.gradle").exists());

		File portletFile = IO.getFile(
			"generated/test/workspace/modules/apps/gradle.test/src/main/java/gradle/test/FooPortlet.java");

		assertTrue(portletFile.exists());

		String portletFileContent = new String(IO.read(portletFile));

		contains(portletFileContent, "^package gradle.test;.*");

		contains(portletFileContent,
			".*javax.portlet.display-name=Gradle.test.*");

		contains(portletFileContent, ".*^public class FooPortlet .*");

		contains(portletFileContent,
			".*printWriter.print\\(\\\"Gradle.test Portlet.*");

		File bndFile = IO.getFile("generated/test/workspace/modules/apps/gradle.test/bnd.bnd");

		assertTrue(bndFile.exists());

		String bndFileContent = new String(IO.read(bndFile));

		contains(bndFileContent, ".*^Private-Package: \\\\.*^\tgradle.test$.*");

		File gradleBuildFile = IO.getFile("generated/test/workspace/modules/apps/gradle.test/build.gradle");

		assertTrue(gradleBuildFile.exists());

		String gradleBuildFileContent = new String(IO.read(gradleBuildFile));

		lacks(gradleBuildFileContent,
			".*^apply plugin: \"com.liferay.plugin\".*");
	}

	@Test
	public void createGradleServicePreAction() throws Exception {
		String [] args = {
				"create",
				"-d",
				"generated/test",
				"-t",
				"service",
				"-s",
				"com.liferay.portal.kernel.events.LifecycleAction",
				"-c",
				"FooAction",
				"servicepreaction"
		};

		blade.main(args);

		File buildFile =
			IO.getFile("generated/test/servicepreaction/build.gradle");

		assertTrue(buildFile.exists());

		File serviceFile = IO.getFile(
			"generated/test/servicepreaction/src/main/java/servicepreaction/FooAction.java");

		assertTrue(serviceFile.exists());

		String serviceFileContent = new String(IO.read(serviceFile));

		contains(serviceFileContent, "^package servicepreaction;.*");

		contains(serviceFileContent,
			".*^import com.liferay.portal.kernel.events.LifecycleAction;$.*");

		contains(serviceFileContent, ".*service = LifecycleAction.class.*");

		contains(serviceFileContent,
			".*^public class FooAction implements LifecycleAction \\{.*");

		File bndFile = IO.getFile("generated/test/servicepreaction/bnd.bnd");

		assertTrue(bndFile.exists());

		String bndFileContent = new String(IO.read(bndFile));

		contains(
			bndFileContent, ".*com.liferay.portal.service;version=\"7.0.0\".*");
	}

	@Test
	public void createGradleServiceWrapper() throws Exception {
		String [] args = {
				"create",
				"-d",
				"generated/test",
				"-t",
				"servicewrapper",
				"-s",
				"com.liferay.portal.service.UserLocalServiceWrapper",
				"serviceoverride"
		};

		blade.main(args);

		File buildFile =
			IO.getFile("generated/test/serviceoverride/build.gradle");

		assertTrue(buildFile.exists());

		File serviceWrapperFile = IO.getFile(
			"generated/test/serviceoverride/src/main/java/serviceoverride/Serviceoverride.java");

		assertTrue(serviceWrapperFile.exists());

		String serviceWrapperFileContent = new String(IO.read(serviceWrapperFile));

		contains(serviceWrapperFileContent, "^package serviceoverride;.*");

		contains(serviceWrapperFileContent,
			".*^import com.liferay.portal.service.UserLocalServiceWrapper;$.*");

		contains(serviceWrapperFileContent, ".*service = ServiceWrapper.class.*");

		contains(serviceWrapperFileContent,
			".*^public class Serviceoverride extends UserLocalServiceWrapper \\{.*");

		contains(serviceWrapperFileContent,
			".*public Serviceoverride\\(\\) \\{.*");

		File bndFile = IO.getFile("generated/test/serviceoverride/bnd.bnd");

		assertTrue(bndFile.exists());

		String bndFileContent = new String(IO.read(bndFile));

		contains(
			bndFileContent, ".*^Private-Package: \\\\.*^\tserviceoverride.*");

		contains(
			bndFileContent, ".*com.liferay.portal.service;version=\'7.0.0\'.*");

	}

	@Test
	public void createGradleServiceBuilder() throws Exception {
		String [] args = {
				"create",
				"-d",
				"generated/test",
				"-t",
				"servicebuilder",
				"-p",
				"com.liferay.docs.guestbook",
				"guestbook"
		};

		blade.main(args);

		File settingsFile =
			IO.getFile("generated/test/guestbook/settings.gradle");

		assertTrue(settingsFile.exists());

		String settingsFileContent = new String(IO.read(settingsFile));

		contains(
			settingsFileContent,
			"include 'com.liferay.docs.guestbook.api','com.liferay.docs.guestbook.svc','com.liferay.docs.guestbook.web'");

		File apiBndFile = IO.getFile(
			"generated/test/guestbook/com.liferay.docs.guestbook.api/bnd.bnd");

		assertTrue(apiBndFile.exists());

		String apiBndFileContent = new String(IO.read(apiBndFile));

		contains(apiBndFileContent, ".*Export-Package\\: \\\\.*");
		contains(apiBndFileContent, ".*com.liferay.docs.guestbook.exception\\,\\\\.*");
		contains(apiBndFileContent, ".*com.liferay.docs.guestbook.model\\,\\\\.*");
		contains(apiBndFileContent, ".*com.liferay.docs.guestbook.service\\,\\\\.*");
		contains(apiBndFileContent, ".*com.liferay.docs.guestbook.service.persistence.*");

		File svcBndFile = IO.getFile(
			"generated/test/guestbook/com.liferay.docs.guestbook.svc/bnd.bnd");

		assertTrue(svcBndFile.exists());

		String svcBndFileContent = new String(IO.read(svcBndFile));

		contains(svcBndFileContent, ".*Private-Package\\: \\\\.*");
		contains(svcBndFileContent, ".*com.liferay.docs.guestbook.model.impl\\,\\\\.*");
		contains(svcBndFileContent, ".*com.liferay.docs.guestbook.service.base\\,\\\\.*");
		contains(svcBndFileContent, ".*com.liferay.docs.guestbook.service.http\\,\\\\.*");
		contains(svcBndFileContent, ".*com.liferay.docs.guestbook.service.impl\\,\\\\.*");
		contains(svcBndFileContent, ".*com.liferay.docs.guestbook.service.persistence.impl\\,\\\\.*");
		contains(svcBndFileContent, ".*com.liferay.docs.guestbook.service.util.*");

		File webBndFile = IO.getFile(
			"generated/test/guestbook/com.liferay.docs.guestbook.web/bnd.bnd");

		assertTrue(webBndFile.exists());

		String webBndFileContent = new String(IO.read(webBndFile));

		contains(webBndFileContent, ".*Private-Package\\: \\\\.*");
		contains(webBndFileContent, ".*com.liferay.docs.guestbook.web.*");

		File portletFile = IO.getFile(
			"generated/test/guestbook/com.liferay.docs.guestbook.web/src/main/java" +
			"/com/liferay/docs/guestbook/portlet/GuestbookPortlet.java");

		assertTrue(portletFile.exists());

		String portletFileContent = new String(IO.read(portletFile));

		contains(portletFileContent, ".*package com.liferay.docs.guestbook.portlet;.*");
	}

	@Test
    public void createGradleJspHook() throws Exception {
        String [] args = {
                "create",
                "-d",
                "generated/test",
                "-t",
                "jsphook",
                "-h",
                "com.liferay.login.web",
                "-H",
                "1.0.0",
                "loginHook"
        };

        blade.main(args);

        assertTrue(IO.getFile("generated/test/loginHook").exists());

        File bndFile = new File("generated/test/loginHook/bnd.bnd");

        assertTrue(bndFile.exists());

        String bndFileContent = new String(IO.read(bndFile));

        contains(
            bndFileContent,
            ".*^Bundle-SymbolicName: LoginHook.*$");
        contains(
            bndFileContent,
            ".*^Fragment-Host: com.liferay.login.web;bundle-version=\"1.0.0\".*$");

        File gradleBuildFile = IO.getFile("generated/test/loginHook/build.gradle");

        assertTrue(gradleBuildFile.exists());

        String gradleBuildFileContent = new String(IO.read(gradleBuildFile));

        contains(gradleBuildFileContent,
            ".*^apply plugin: \"com.liferay.plugin\".*");
	}

	@Test
    public void createWorkspaceGradleJspHook() throws Exception {
	    String [] args = {
            "create",
            "-d",
            "generated/test/workspace/modules/hooks",
            "-t",
            "jsphook",
            "-h",
            "com.liferay.login.web",
            "-H",
            "1.0.0",
            "loginHook"
	    };

        File workspace = new File("generated/test/workspace");

        makeWorkspace(workspace);

        blade.main(args);

        assertTrue(IO.getFile("generated/test/workspace/modules/hooks/loginHook").exists());

        File bndFile = new File("generated/test/workspace/modules/hooks/loginHook/bnd.bnd");

        assertTrue(bndFile.exists());

        String bndFileContent = new String(IO.read(bndFile));

        contains(
            bndFileContent,
            ".*^Bundle-SymbolicName: LoginHook.*$");
        contains(
            bndFileContent,
            ".*^Fragment-Host: com.liferay.login.web;bundle-version=\"1.0.0\".*$");

        assertTrue(
            IO.getFile("generated/test/workspace/modules/hooks/loginHook/build.gradle").exists());

        File gradleBuildFile = IO.getFile("generated/test/workspace/modules/hooks/loginHook/build.gradle");

        assertTrue(gradleBuildFile.exists());

        String gradleBuildFileContent = new String(IO.read(gradleBuildFile));

        lacks(gradleBuildFileContent,
            ".*^apply plugin: \"com.liferay.plugin\".*");
	}

	@Before
	public void setup() throws Exception {
		File testdir = IO.getFile("generated/test");

		if (testdir.exists()) {
			IO.delete(testdir);
			assertFalse(testdir.exists());
		}
	}

	private void lacks(String content, String pattern) {
		assertFalse(
			Pattern.compile(
				pattern, Pattern.MULTILINE | Pattern.DOTALL).matcher(
					content).matches());
	}

	private void contains(String content, String pattern) {
		assertTrue(
			Pattern.compile(
				pattern, Pattern.MULTILINE | Pattern.DOTALL).matcher(
					content).matches());
	}

}
