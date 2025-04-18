package br.com.lucasaraujo.api.controllers;



import br.com.lucasaraujo.PageQuery;
import br.com.lucasaraujo.PageResponse;
import br.com.lucasaraujo.api.mappers.fotoPessoa.FotoMapper;
import br.com.lucasaraujo.api.mappers.servidor.ServidorTemporarioMapper;
import br.com.lucasaraujo.dto.fotoPessoa.FotoRequest;
import br.com.lucasaraujo.dto.fotoPessoa.FotoResponse;
import br.com.lucasaraujo.dto.servidor.ServidorTemporarioRequest;
import br.com.lucasaraujo.dto.servidor.ServidorTemporarioResponse;
import br.com.lucasaraujo.model.ServidorTemporarioModel;
import br.com.lucasaraujo.service.Resource;
import br.com.lucasaraujo.stories.fotoPessoa.FotoPessoaUseStory;
import br.com.lucasaraujo.stories.servidor.ServidorTemporarioUseStory;
import br.com.lucasaraujo.util.HashingUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Servidores Temporários")
@RequestMapping("/servidorTemporario")
public class ServidorTemporarioController {

    private final ServidorTemporarioMapper servidorTemporarioMapper;

    private final ServidorTemporarioUseStory servidorTemporarioUseStory;

    private final FotoMapper fotoMapper;

    private final FotoPessoaUseStory fotoPessoaUseStory;

    public ServidorTemporarioController(ServidorTemporarioMapper servidorTemporarioMapper, ServidorTemporarioUseStory servidorTemporarioUseStory, FotoMapper fotoMapper, FotoPessoaUseStory fotoPessoaUseStory) {
        this.servidorTemporarioMapper = servidorTemporarioMapper;
        this.servidorTemporarioUseStory = servidorTemporarioUseStory;
        this.fotoMapper = fotoMapper;
        this.fotoPessoaUseStory = fotoPessoaUseStory;
    }

    @Operation(summary = "Criar um novo servidor temporário")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Servidor temporário criado com sucesso"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada")
    })

    @PostMapping()
    public ServidorTemporarioResponse criarServidorTemporario(
            @RequestBody ServidorTemporarioRequest servidorTemporarioRequest
    ) {

        return  servidorTemporarioMapper.servidorTemporarioModelToResponse(servidorTemporarioUseStory
                .criar(servidorTemporarioMapper.servidorTemporarioRequestToModel(servidorTemporarioRequest)));

    }

    @Operation(summary = "Atualizar um servidor temporário pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Servidor temporário atualizado com sucesso"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada"),
            @ApiResponse(responseCode  = "404", description  = "Serviço não encontrado")
    })
    @PutMapping("/{pessoaId}")
    public ServidorTemporarioResponse atualizarServidorTemporario(@PathVariable Long pessoaId,
                                                                  @RequestBody ServidorTemporarioRequest servidorTemporarioRequest
    ) {

        return servidorTemporarioMapper.servidorTemporarioModelToResponse(servidorTemporarioUseStory
                .atualizar(pessoaId,servidorTemporarioMapper.servidorTemporarioRequestToModel(servidorTemporarioRequest)));
    }

    @Operation(summary = "Fazer upload de fotos de um servidor temporário")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Upload de fotos doServidor temporário enviado com sucesso"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada"),
            @ApiResponse(responseCode  = "404", description  = "Serviço não encontrado")
    })

    @PostMapping(value = "/uploadFoto/{pessoaId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public List<FotoResponse> uploadFotos(
            @PathVariable Long pessoaId,
            @RequestParam(name = "fotos", required = false) List<MultipartFile> fotos
    ){
        List<Resource> listaResource = fotos.stream().map(this::resourceOf).toList();
        List<FotoResponse>listaFotoResponse =new ArrayList<FotoResponse>();
        List<FotoRequest>listaFotoRequest =new ArrayList<FotoRequest>();

        listaResource.forEach((f)->{
            FotoRequest fotoRequest = new FotoRequest(pessoaId,f);
            listaFotoRequest.add(fotoRequest);
        });

        listaFotoResponse =  fotoMapper.fotoModelListToFotoResponseList(fotoPessoaUseStory
                .uploadFotos(fotoMapper.fotoRequestListToFotoModelList(listaFotoRequest)));


        return listaFotoResponse;
    }

    @Operation(summary = "Excluir uma Servidor temporário pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Servido efetivo excluido com sucesso"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada"),
            @ApiResponse(responseCode  = "404", description  = "Serviço não encontrado")
    })
    @DeleteMapping("/{pessoaId}")
    public ResponseEntity<String> excluir(@PathVariable Long pessoaId) {
        servidorTemporarioUseStory.excluir(pessoaId);
        servidorTemporarioUseStory.excluir(pessoaId);
        return ResponseEntity.ok("Servidor temporario excluido com sucesso");
    }

    @Operation(summary = "Buscar um servidor temporário pelo Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Servidor temporário buscado pelo Id com sucesso"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada"),
            @ApiResponse(responseCode  = "404", description  = "Serviço não encontrado")
    })
    @GetMapping("/{pessoaId}")
    public ServidorTemporarioResponse buscarServidorTemporarioPorId(@PathVariable Long pessoaId) {
        return servidorTemporarioMapper.servidorTemporarioModelToResponse(servidorTemporarioUseStory
                .buscarPorId(pessoaId));
    }


    @Operation(summary = "Listar servidores temporários de forma paginado")
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description  = "Servidores temporários listadas de forma paginado"),
            @ApiResponse(responseCode  = "400", description  = "Requisição inválida"),
            @ApiResponse(responseCode  = "403", description  = "Requisição não autorizada"),
            @ApiResponse(responseCode  = "404", description  = "Serviço não encontrado")
    })
    @GetMapping("/paginado/all")
    public PageResponse<ServidorTemporarioResponse> servidorTemporarioUseStory(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int sizePage) {
        PageQuery pageQuery = new PageQuery(page, sizePage);
        PageResponse<ServidorTemporarioModel> unidadePage = servidorTemporarioUseStory.listaServidoresTemporariosPaginado(pageQuery);

        return unidadePage.map(servidorTemporarioMapper::servidorTemporarioModelToResponse);
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) {
            return null;
        }

        try {
            return Resource.with(
                    part.getBytes(),
                    HashingUtils.checksum(part.getBytes()),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}