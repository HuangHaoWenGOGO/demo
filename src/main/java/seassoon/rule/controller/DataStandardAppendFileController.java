package seassoon.rule.controller;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import seassoon.rule.entity.DataStandard;
import seassoon.rule.entity.DataStandardAppendFile;
import seassoon.rule.exception.CustomException;
import seassoon.rule.exception.CustomExceptionMessage;
import seassoon.rule.service.DataSetReviewerConfigService;
import seassoon.rule.service.DataStandardAppendFileService;
import seassoon.rule.service.DataStandardService;
import seassoon.rule.utils.CopyUtils;
import seassoon.rule.utils.FileDownloadUtils;
import seassoon.rule.utils.ZipUtils;
import seassoon.rule.vo.DataStandardAppendFileVO;

/**
 * <p>
 * 数据标准关联的文件表 前端控制器
 * </p>
 *
 * @author Wayne
 * @since 2020-08-03
 */
@RestController
@RequestMapping("/data-standard-append-file")
public class DataStandardAppendFileController {
	
	@Autowired
	private DataStandardService dataStandardService;
	
	@Autowired
	private DataStandardAppendFileService dataStandardAppendFileService;
	
	@Autowired
	private DataSetReviewerConfigService dataSetReviewerConfigService;
	
	@Value("${file.filepath}")
    private String filePath;
	
	/**
	 * 
	 * @param file
	 * @param id   dataStandardId
	 * @param dataSetId
	 * @return
	 */
	@ApiOperation("上传测试")
	@PostMapping("upload")
	public DataStandardAppendFileVO upload(@RequestParam("uploadFile") MultipartFile file,@RequestParam("dataStandardId") Integer dataStandardId,@RequestParam("dataSetId") Integer dataSetId){
		
		if(dataSetReviewerConfigService.findDataSetReviewerByDataSetId(dataSetId).size()<1) {
			throw new CustomException.BadRequestException(CustomExceptionMessage.REVIEWER_IS_NOT_EXIST);
		}
		
		DataStandardAppendFile dataStandardAppendFile=new DataStandardAppendFile();
		DataStandardAppendFileVO dataStandardAppendFileVO = new DataStandardAppendFileVO();
		dataStandardAppendFileVO.setDataSetId(dataSetId);
        try {
            if (file.isEmpty()){
                return dataStandardAppendFileVO;
            }
            //获取文件名
            String fileName = file.getOriginalFilename();
           System.out.println("上传的文件名："+fileName);
            //获取文件后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            System.out.println("文件后缀名："+suffixName);
            //设置文件存储路径
            System.out.println(filePath);
            String uuid = UUID.randomUUID().toString().replaceAll("-","");
            String path = filePath+uuid+suffixName;
            String saveFileName=uuid+suffixName;
            System.out.println(uuid);
            File dest = new File(path);
            //检测是否存在该目录
            if (!dest.getParentFile().exists()){
                dest.getParentFile().mkdirs();
            }
            //写入文件
            file.transferTo(dest);
            
            if(dataStandardId<1) {
            	//add
            	
            	//写一条空datastandard
            	DataStandard dataStandard =new DataStandard();
            	dataStandard.setDataSetId(dataSetId);
            	dataStandard.setStatus(3);
            	dataStandardService.save(dataStandard);
            	
            	dataStandardAppendFile.setFileName(saveFileName);      
            	dataStandardAppendFile.setFilePath(filePath);
            	dataStandardAppendFile.setFileType(suffixName);
            	dataStandardAppendFile.setDataStandardId(dataStandard.getId());
            	dataStandardAppendFileService.save(dataStandardAppendFile);
            	
            }else {
            	//update data_standard
            	dataStandardAppendFile.setFileName(saveFileName);      
            	dataStandardAppendFile.setFilePath(filePath);
            	dataStandardAppendFile.setFileType(suffixName);
            	dataStandardAppendFile.setDataStandardId(dataStandardId);
            	dataStandardAppendFileService.save(dataStandardAppendFile);
            	
            }
             
            CopyUtils.copyProperties(dataStandardAppendFile, dataStandardAppendFileVO);
            
            return dataStandardAppendFileVO;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataStandardAppendFileVO;
    }
	
	@ApiOperation("上传多个文件测试")
	@PostMapping("uploadFiles")
	public DataStandardAppendFileVO uploadFiles(@RequestParam("uploadFile") MultipartFile[] files,@RequestParam("dataStandardId") Integer dataStandardId,@RequestParam("dataSetId") Integer dataSetId){
		
		
		if(dataSetReviewerConfigService.findDataSetReviewerByDataSetId(dataSetId).size()<1) {
			throw new CustomException.BadRequestException(CustomExceptionMessage.REVIEWER_IS_NOT_EXIST);
		}
		
		DataStandardAppendFile dataStandardAppendFileBackSingle=new DataStandardAppendFile();
		DataStandardAppendFileVO dataStandardAppendFileVO = new DataStandardAppendFileVO();
		dataStandardAppendFileVO.setDataSetId(dataSetId);
		
		//MultipartFile file=files[0];
		System.out.println("======================");
		for(MultipartFile ff:files) {
			
			DataStandardAppendFile dataStandardAppendFile=new DataStandardAppendFile();
			System.out.println(ff.getOriginalFilename());
			try {
				if (ff.isEmpty()){
	                return dataStandardAppendFileVO;
	            }
				//获取文件名
	            String fileName = ff.getOriginalFilename();
	            System.out.println("上传的文件名："+fileName);
	            //获取文件后缀名
	            String suffixName = fileName.substring(fileName.lastIndexOf("."));
	            System.out.println("文件后缀名："+suffixName);
	            //设置文件存储路径
	            System.out.println(filePath);
	            String uuid = UUID.randomUUID().toString().replaceAll("-","");
	            String path = filePath+uuid+suffixName;
	            String saveFileName=uuid+suffixName;
	            System.out.println(uuid);
	            File dest = new File(path);
	            //检测是否存在该目录
	            if (!dest.getParentFile().exists()){
	                dest.getParentFile().mkdirs();
	            }
	            //写入文件
	            ff.transferTo(dest);
	            if(dataStandardId<1) {
	            	//add
	            	//写一条空datastandard
	            	DataStandard dataStandard =new DataStandard();
	            	dataStandard.setDataSetId(dataSetId);
	            	dataStandard.setStatus(3);
	            	dataStandardService.save(dataStandard);
	            	dataStandardId = dataStandard.getId();
	            	dataStandardAppendFile.setFileName(saveFileName);      
	            	dataStandardAppendFile.setFilePath(filePath);
	            	dataStandardAppendFile.setFileType(suffixName);
	            	dataStandardAppendFile.setDataStandardId(dataStandardId);
	            	dataStandardAppendFileService.save(dataStandardAppendFile);
	            	
	            	
	            }else {
	            	
	            	//update data_standard
	            	dataStandardAppendFile.setFileName(saveFileName);      
	            	dataStandardAppendFile.setFilePath(filePath);
	            	dataStandardAppendFile.setFileType(suffixName);
	            	dataStandardAppendFile.setDataStandardId(dataStandardId);
	            	dataStandardAppendFileService.save(dataStandardAppendFile);
	            	
	            }
	            
	            dataStandardAppendFileBackSingle = dataStandardAppendFile;
				
			}catch (IOException e) {
	            e.printStackTrace();
	        }
			
		}
		 CopyUtils.copyProperties(dataStandardAppendFileBackSingle, dataStandardAppendFileVO);
        return dataStandardAppendFileVO;
    }
	
	
	
	@ApiOperation("清除刚刚上传的文件")
	@DeleteMapping("{id}")
    public boolean delete(@PathVariable Integer id) {
		
		//清理文件
		
    	return dataStandardAppendFileService.removeById(id);
    } 
	
	@ApiOperation("下载单个文件")
	@GetMapping("{id}/downloadFile")
	public void downloadFile(@PathVariable Integer id,HttpServletResponse response) {
		
		List<File> fileList =new ArrayList<File>();
		DataStandardAppendFile dataStandardAppendFile = dataStandardAppendFileService.getById(id);
		String downloadfile=dataStandardAppendFile.getFileName();
		String downloadfileWithPath=filePath+downloadfile;
		System.out.println(downloadfileWithPath);
		
		File file = new File(downloadfileWithPath);
		
		fileList.add(file);
		
		System.out.println(dataStandardAppendFile.getFileName()+dataStandardAppendFile.getFileType());
		
		FileDownloadUtils.downloadReport(file, dataStandardAppendFile.getFileName(), response);
	}
	
	@ApiOperation("下载多个文件")
	@PostMapping("/downloadFiles")
	public void downloadFiles(@RequestBody List<String> fileIds,HttpServletResponse response) throws IOException {
		List<DataStandardAppendFile> dsFileList = dataStandardAppendFileService.listByIds(fileIds);
		List<File> fileObList =new ArrayList<File>();
		for(DataStandardAppendFile df:dsFileList) {
			String downloadfileWithPath=filePath+df.getFileName();
			System.out.println(df.getFileName());
			fileObList.add(new File(downloadfileWithPath));
		}
		//新建一个随机文件名的压缩包文件
		String uuid = UUID.randomUUID().toString().replaceAll("-","");
		String zipFile = uuid+".zip";
		String saveFileNameWith=filePath+zipFile;
		File file = new File(saveFileNameWith);
		OutputStream fileWriter = new FileOutputStream(file);
		ZipUtils.toZip(fileObList, fileWriter);
		FileDownloadUtils.downloadReport(file, zipFile, response);
	}
	
	@ApiOperation("文件多选删除")
	@PostMapping("/multipleDelete")
	public boolean multipleDeleteFiles(@RequestBody List<String> ids) throws IOException {
		
		
		return dataStandardAppendFileService.removeByIds(ids);
	}
	
	
}
