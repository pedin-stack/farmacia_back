package br.com.personal.remedio.service;

import br.com.personal.insfrastructre.role.StatusRole;
import br.com.personal.remedio.dto.RemedioRequestDTO;
import br.com.personal.remedio.entity.Remedio;
import br.com.personal.remedio.repository.RemedioRepository;
// IMPORTANTE: Adicione o import do repositório de Pessoa
import br.com.personal.pessoa.repository.PessoaRepository;
import br.com.personal.pessoa.entity.Pessoa;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper; // Adicione o ModelMapper se for usar aqui, ou faça manual
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RemedioService {

    private final RemedioRepository remedioRepository;
    private final PessoaRepository pessoaRepository;
    private final ModelMapper modelMapper;

    public Page<Remedio> findAll(Pageable pageable) {
        return remedioRepository.findAll(pageable);
    }

    public Remedio findById(Long id) {
        return remedioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Remédio não encontrado com o ID: " + id));
    }

    @Transactional
    public Remedio save(RemedioRequestDTO dto) {

        Remedio remedio = modelMapper.map(dto, Remedio.class);
        remedio.setId(null);

        if (dto.getPessoaId() != null) {
            Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                    .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com ID: " + dto.getPessoaId()));
            remedio.setPessoa(pessoa);
        }

        calcularProximaCompraEStatus(remedio);

        return remedioRepository.save(remedio);
    }

    @Transactional
    public Remedio update(Long id, RemedioRequestDTO dto) {
        Remedio remedio = findById(id);

        remedio.setNome(dto.getNome());
        remedio.setQuantidade(dto.getQuantidade());
        remedio.setUsoDiario(dto.getUsoDiario());
        remedio.setHoraConsumo(dto.getHoraConsumo());

        calcularProximaCompraEStatus(remedio);

        return remedioRepository.save(remedio);
    }

    @Transactional
    public void delete(Long id) {
        Remedio remedio = findById(id);
        remedioRepository.delete(remedio);
    }

    private void calcularProximaCompraEStatus(Remedio remedio) {
        if (remedio.getQuantidade() > 0 && remedio.getUsoDiario() > 0) {
            long diasDuracao = (long) (remedio.getQuantidade() / remedio.getUsoDiario());
            LocalDate dataPrevista = LocalDate.now().plusDays(diasDuracao);
            remedio.setProxCompra(dataPrevista);
        } else {
            remedio.setProxCompra(LocalDate.now());
        }
        definirStatus(remedio);
    }

    private void definirStatus(Remedio remedio) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataCompra = remedio.getProxCompra();
        long diasRestantes = ChronoUnit.DAYS.between(hoje, dataCompra);

        if (remedio.getQuantidade() <= 0 || diasRestantes <= 3) {
            remedio.setStatus(StatusRole.URGENTE);
        } else if (diasRestantes <= 5) {
            remedio.setStatus(StatusRole.ATENCAO);
        } else {
            remedio.setStatus(StatusRole.NORMAL);
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
   //@Scheduled(fixedRate = 10000)//teste
    @Transactional
    public void atualizarEstoqueDiario() {
       // System.out.println("--- Iniciando atualização automática de estoque ---");
        List<Remedio> remedios = remedioRepository.findAll();
        for (Remedio remedio : remedios) {
            double novaQuantidade = remedio.getQuantidade() - remedio.getUsoDiario();
            if (novaQuantidade < 0) novaQuantidade = 0;
            remedio.setQuantidade(novaQuantidade);
            calcularProximaCompraEStatus(remedio);
        }
        remedioRepository.saveAll(remedios);
        //System.out.println("--- Estoque atualizado com sucesso!! ---");
    }
    @Scheduled(cron = "0 0 3 * * *") // todo dia, as 3 da manha vou chamar essa função para manter o banco ligado
    public void manterBancoLigado(){

        Remedio remedioPaleativo = new Remedio();

       remedioPaleativo.setNome("iasjdkjosahdasjhgdsakjhdsakljdh");
       remedioPaleativo.setQuantidade(3);
       remedioPaleativo.setUsoDiario(3);
       remedioPaleativo.setProxCompra(LocalDate.now());
       remedioPaleativo.setStatus(StatusRole.URGENTE);
       remedioRepository.save(remedioPaleativo);
       remedioRepository.delete(remedioPaleativo);
       remedioRepository.flush();


    }
}