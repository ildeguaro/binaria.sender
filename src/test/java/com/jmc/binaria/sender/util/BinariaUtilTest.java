package com.jmc.binaria.sender.util;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.jmc.binaria.sender.model.Campaign;

@RunWith(JUnit4.class)
public class BinariaUtilTest {

	@Test
	public void separarPdf() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		Campaign campaign = new Campaign();
		campaign.setId(1);
		BinariaUtil.separarDocumentosYEncolarEnvioPorPdf(new File("/tmp/PAQUETE_001_WILLIAM.pdf"), "/tmp/separacion/",
				campaign);
	}

}
