package br.com.codenation;

import br.com.codenation.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.exceptions.TimeNaoEncontradoException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DesafioMeuTimeApplication implements MeuTimeInterface {


	List<Time> timesList = new ArrayList<>();
	List<Jogador> jogadoresList = new ArrayList<>();

	//1
	public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {

		if(timesList.stream().anyMatch(x -> x.getId() == id)){
			throw new IdentificadorUtilizadoException();
		}

		timesList.add(new Time(id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario));

	}
	//2
	public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {


		if(jogadoresList.stream().anyMatch(x -> x.getId() == id)){
			throw new IdentificadorUtilizadoException();
		}

		if(timesList.stream().noneMatch(x -> x.getId() == idTime)){
			throw new TimeNaoEncontradoException();
		}

		jogadoresList.add(new Jogador(id, idTime, nome, dataNascimento, nivelHabilidade, salario));


	}
	//3
	public void definirCapitao(Long idJogador) {


		Jogador capitao = jogadoresList.stream()
				.filter(x -> x.getId() == idJogador)
				.findAny()
				.orElseThrow(JogadorNaoEncontradoException::new);

		jogadoresList.stream()
				.filter(x -> capitao.getIdTime().equals(x.getIdTime()))
				.forEach(x -> x.setCapitao(false));

		capitao.setCapitao(true);





	}
	//4
	public Long buscarCapitaoDoTime(Long idTime) {

		if(timesList.stream().noneMatch(x -> x.getId() == idTime)){
			throw new TimeNaoEncontradoException();
		}
		if(jogadoresList.stream().noneMatch(x -> x.getIdTime() == idTime && x.getCapitao() == true)){
			throw new CapitaoNaoInformadoException();
		}

		Jogador capitao = jogadoresList.stream()
				.filter(x -> x.getIdTime() == idTime && x.getCapitao() == true)
				.findAny()
				.orElse(null);
		return capitao.getId();
	}
	//5
	public String buscarNomeJogador(Long idJogador) {
		Jogador jogador = jogadoresList.stream().
				filter(x -> x.getId() == idJogador)
				.findFirst()
				.orElseThrow(JogadorNaoEncontradoException::new);

		return  jogador.getNome();

	}
	//6
	public String buscarNomeTime(Long idTime) {

		Time time = timesList.stream()
				.filter(x -> x.getId() == idTime)
				.findFirst()
				.orElseThrow(TimeNaoEncontradoException::new);
		return time.getNome();
	}
	//7
	public List<Long> buscarJogadoresDoTime(Long idTime) {

		if(jogadoresList.stream().noneMatch(x -> x.getIdTime() == idTime)){
			throw new TimeNaoEncontradoException();
		}

		return jogadoresList.stream()
				.filter(x -> x.getIdTime() == idTime)
				.map(x -> x.getId())
				.collect(Collectors.toList());

	}
	//8
	public Long buscarMelhorJogadorDoTime(Long idTime) {


		return jogadoresList.stream()
				.filter(x -> x.getIdTime() == idTime)
				.max(Comparator.comparingInt(x -> x.getNivelHabilidade()))
				.map(x -> x.getId())
				.orElseThrow(TimeNaoEncontradoException::new);
	}
	//9
	public Long buscarJogadorMaisVelho(Long idTime) {
		return jogadoresList.stream()
				.filter(x -> x.getIdTime() == idTime)
				.max(Comparator.comparing(Jogador::getDataNascimento).reversed())
				.map(x -> x.getId())
				.orElseThrow(TimeNaoEncontradoException::new);
	}
	//10
	public List<Long> buscarTimes() {
		return timesList.stream()
				.filter(x -> x.getId() != null)
				.map(x -> x.getId())
				.collect(Collectors.toList());
	}
	//11
	public Long buscarJogadorMaiorSalario(Long idTime) {
		return jogadoresList.stream()
				.filter(x -> x.getIdTime() == idTime)
				.max(Comparator.comparing(Jogador::getSalario))
				.map(x -> x.getId())
				.orElseThrow(TimeNaoEncontradoException::new);
	}
	//12
	public BigDecimal buscarSalarioDoJogador(Long idJogador) {
		Jogador jogador = jogadoresList.stream()
				.filter(x -> x.getId() == idJogador)
				.findFirst().orElseThrow(JogadorNaoEncontradoException::new);

		return jogador.getSalario();
	}

	//13
	public List<Long> buscarTopJogadores(Integer top) {

		return jogadoresList.stream()
				.sorted(Comparator.comparing(Jogador::getNivelHabilidade).reversed())
				.map(Jogador::getId)
				.limit(top)
				.collect(Collectors.toList());


	}





}
